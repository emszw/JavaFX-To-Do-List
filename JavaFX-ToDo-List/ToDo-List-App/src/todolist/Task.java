package todolist;

public class Task {
    private String title;
    private String description;
    private String category;
    private String deadline;
    private boolean complete;
    private String status = "INCOMPLETE";

    public Task(String title, String description, String deadline, String category, boolean complete) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.category = category;
        this.complete = complete;  
    }

    public Task (){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String printStatement() {
        String taskString = "Task info: \n" +
        "Category: "+getCategory()+", Title: "+getTitle()+", Deadline: "+getDeadline()+", Status: "+getStatus()+"\n"; 
        return taskString;
    }
}
