import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<SubTask> subTasks = new ArrayList<>();

    public Epic(Integer id, String nameTask, String descriptionTask, String statusTask) {
        super(id, nameTask, descriptionTask, statusTask);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }



    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    //Проверка статуса

    public String getStatus() {
        if (subTasks.isEmpty()) {
            return "New";
        }

        int newSubTasksCount = 0;
        int doneSubTasksCount = 0;

        for (SubTask subTask : subTasks) {
            if (subTask.statusTask.equals("New")) {
                newSubTasksCount++;
            } else if (subTask.statusTask.equals("Done")) {
                doneSubTasksCount++;
            }
        }

        if (newSubTasksCount == subTasks.size()) {
            return "New";
        } else if (doneSubTasksCount == subTasks.size()) {
            return "Done";
        } else {
            return "In progress";
        }
    }


}


