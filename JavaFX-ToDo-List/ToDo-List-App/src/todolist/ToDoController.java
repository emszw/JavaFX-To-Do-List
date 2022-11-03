package todolist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.stage.FileChooser;


public class ToDoController {

    @FXML
    private AnchorPane anchorPane;

    //fxml variables for "All Tasks" tab and table items
    @FXML
    private TableView<Task> tasksTable;
    @FXML
    private TableColumn<Task, String> tasksCatCol;
    @FXML
    private TableColumn<Task, String> tasksTitleCol;
    @FXML
    private TableColumn<Task, String> tasksDescCol;
    @FXML
    private TableColumn<Task, String> tasksDeadlineCol;
    @FXML
    private TableColumn<Task, String> tasksStatusCol;
    @FXML
    private TableColumn<Task, Task> completeBtnCol;
    @FXML
    private TableColumn<Task, Task> deleteBtnCol;
    

    //fxml variables for "Add New Task"
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker deadlinePicker;

    //Radiobuttons for categories
    @FXML
    private ToggleGroup getCategory;
    @FXML
    private RadioButton personalRdButtton, schoolRdButtton, workRdButtton, errandsRdButton, goalsRdButtton, otherRdButtton;
    @FXML
    private Button addNewTaskButton;

    //Save and Load buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;

    //Assigning the category enums to radiobuttons
    @FXML
    void handleRadioButtons(ActionEvent event) {

        personalRdButtton.setUserData("Personal");
        schoolRdButtton.setUserData("School");
        workRdButtton.setUserData("Work");
        errandsRdButton.setUserData("Errands");
        goalsRdButtton.setUserData("Goals");
        otherRdButtton.setUserData("Other");
    }

    //Lists for storing all tasks and displaying on the correct tableview
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ArrayList<Task> tasksArray = new ArrayList<Task>();
    

    //Event handling for addNewTaskButton and adding new task to our todolist 
    @FXML
    void handleAddNewTaskButtonClicked(ActionEvent event) {

        // //getting string value from the selected categoryradio button
        String selectedCategory = (String)getCategory.getSelectedToggle().getUserData();
       
        //converting the LocalDate to String for better format
        LocalDate date = (LocalDate)deadlinePicker.getValue();
        String deadlineFormatted = date.format(DateTimeFormatter.ofPattern("dd-MM-yy"));

        //Cool alert to make sure you fill both textfields out!
        if (titleTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill out all text fields");
            alert.showAndWait();
            return;
        }
        else {
            tasks.add(new Task(titleTextField.getText(), descriptionTextField.getText(), deadlineFormatted, selectedCategory, false)); 
            tasksArray.add(new Task(titleTextField.getText(), descriptionTextField.getText(), deadlineFormatted, selectedCategory, false)); 
            initializeAllTaskTable(); 
            clearInputItems();
        }
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        
        FileChooser chooser = new FileChooser();
        File taskListFile = chooser.showSaveDialog(anchorPane.getScene().getWindow());
        if (taskListFile != null) {
            TaskManager.saveTaskListToDisk(taskListFile, tasksArray);
        }
        tasksTable.setItems(null);
        tasksArray.clear();
    }

    @FXML
    void loadButtonClicked(ActionEvent event) {
        
        try {
        FileChooser chooser = new FileChooser();
            File taskListFile = chooser.showOpenDialog(anchorPane.getScene().getWindow());
            if (taskListFile != null) {
                for (Task task : TaskManager.loadTaskFromFile(taskListFile))
                tasks.add(task);
                initializeAllTaskTable(); 
            }
        } catch (Exception e) {
            System.out.println ("LOG-FILE - Unable to open task list");
        }
    }


    public void initializeAllTaskTable() {

        //assigning each column to correct properties that will display each Task item
        tasksCatCol.setCellValueFactory(new PropertyValueFactory<Task, String>("category"));
        tasksTitleCol.setCellValueFactory(new PropertyValueFactory<Task, String>("title"));
        tasksDescCol.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        tasksDeadlineCol.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));
        tasksStatusCol.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
        deleteBtnCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        completeBtnCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        stylingColumnCells();

        //setting the tableview to all task items
        tasksTable.setItems(tasks);
    }

    //method to clear the input items after adding a new task to the list
    public void clearInputItems() {

        titleTextField.clear();
        descriptionTextField.clear();
        deadlinePicker.setValue(null);
        if (getCategory.getSelectedToggle().isSelected()){
            getCategory.getSelectedToggle().setSelected(false);
        }
        
    }

    public void updateCompleteStatus(Task task) {
        if(task.isComplete() == true){
            task.setComplete(false);
            task.setStatus("INCOMPLETE");
        }
        else{
            task.setComplete(true);
            task.setStatus("COMPLETE");
        }
        //a terminal printout to show confirm item changes within the console (Mostly used for testing if my code works)
        System.out.println("Status updated to "+task.getStatus());
        System.out.println (task.printStatement());
        initializeAllTaskTable(); //recalling the table initializer to update the cells
    }





    //method to center the column cell items because it was driving me insane that I couldn't do it using css or scenebuilder >:(
    //I did a seperate method for string coloumn's so I wouldn't have to write all of this each time
    public void centerStringColumnCells(TableColumn<Task, String> cell) {

        cell.setCellFactory(new Callback<TableColumn<Task, String>, TableCell<Task, String>>() {
            public TableCell<Task, String> call(TableColumn<Task, String> p) {
                TableCell<Task, String> cell = new TableCell<Task, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : getString());
                        setGraphic(null);
                    }
                    private String getString() {
                        return getItem() == null ? "" : getItem().toString();
                    }
                };
                cell.setStyle("-fx-alignment: CENTER;" + "-fx-font-weight: bold");
                return cell;
            }
        });
    }

    public void stylingColumnCells() {

        //calling above method for styling all string coloumns
        centerStringColumnCells(tasksCatCol);
        centerStringColumnCells(tasksTitleCol);
        centerStringColumnCells(tasksDescCol);
        centerStringColumnCells(tasksDeadlineCol);

        //overriding the status cell to set custom text colours. When a task is incomplete it will display as red, if complete it will display green
        tasksStatusCol.setCellFactory(new Callback<TableColumn<Task, String>, TableCell<Task, String>>() {
            public TableCell<Task, String> call(TableColumn<Task, String> p) {
                TableCell<Task, String> cell = new TableCell<Task, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                            return;
                        }   
                        if (item.contains("IN")) {
                            this.setTextFill(Color.RED);
                        } 
                        if (item.equals("COMPLETE")) {
                            this.setTextFill(Color.GREEN);
                        }
                    setText(item);
                    }
                };
                cell.setStyle("-fx-alignment: CENTER;" + "-fx-font-weight: bold");
                return cell;
            }
        });

        //overriding the action column to display a delete button to remove the task from that row.
        deleteBtnCol.setCellFactory(new Callback<TableColumn<Task, Task>, TableCell<Task, Task>>() {
            public TableCell<Task, Task> call(TableColumn<Task, Task> p) {
                TableCell<Task, Task> cell = new TableCell<Task, Task>() {

                private final Button deleteButton = new Button("Delete");
                @Override
                protected void updateItem(Task task, boolean empty) {
                    super.updateItem(task, empty);
                        if(task == null) {
                            setGraphic(null);
                            return;
                        }     
                    setGraphic(deleteButton);
                    deleteButton.setStyle("-fx-background-color: red;" + "-fx-text-fill: white;" + "-fx-font-weight: bold");
                    deleteButton.setOnAction(event -> tasks.remove(task));
                    }
                };
                cell.setStyle("-fx-alignment: CENTER;");
                return cell;
            }
        });

        //overriding the action column to display a delete button to remove the task from that row.
        completeBtnCol.setCellFactory(new Callback<TableColumn<Task, Task>, TableCell<Task, Task>>() {
            public TableCell<Task, Task> call(TableColumn<Task, Task> p) {
                TableCell<Task, Task> cell = new TableCell<Task, Task>() {

                    private final Button markCompleteButton = new Button("Mark as Complete");
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                            if(task == null) {
                                setGraphic(null);
                                return;
                            }
                        setGraphic(markCompleteButton);
                        markCompleteButton.setStyle("-fx-background-color: #006f49;" + "-fx-text-fill: white;" + "-fx-font-weight: bold");
                        markCompleteButton.setOnAction(event -> updateCompleteStatus(task));
                    }
                };
                cell.setStyle("-fx-alignment: CENTER;");
                return cell;
            }
        });
    }
}
