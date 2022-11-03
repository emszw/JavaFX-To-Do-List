package todolist;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {

    public static void saveTaskListToDisk (File file, ArrayList<Task> taskList) {
        String taskDeflatedString = "";
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for(Task task : taskList) {
            taskDeflatedString = task.getTitle() + "###" +
                        task.getDescription() + "###" + 
                        task.getDeadline() + "###" +
                        task.getCategory() + "###" +
                        task.getStatus() + "###";           
            printWriter.println(taskDeflatedString);
            }
        }
        catch(Exception err) {
            System.out.println ("LOG-FILE: Task List was not saved to disk");
        }
    }

    public static ArrayList<Task> loadTaskFromFile(File file) {
        String complete = "COMPLETE";
        ArrayList<Task> tasksArray = new ArrayList<Task>();
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                String[] parsedString = scan.nextLine().split("###");
                boolean completeStatus;
                if (parsedString[4].equals(complete)) {
                    completeStatus = true;
                } else {
                    completeStatus = false;
                }
                tasksArray.add(new Task(parsedString[0],parsedString[1],parsedString[2],parsedString[3],completeStatus));
            }
        } catch (Exception e) {
            System.out.println("File not found!");
        }
        return tasksArray;
    }
}
