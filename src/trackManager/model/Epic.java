package trackManager.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<SubTask> subTasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
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

    public Status getStatus() {
        if (subTasks.isEmpty()) {
            return Status.NEW;
        }

        int newSubTasksCount = 0;
        int doneSubTasksCount = 0;

        for (SubTask subTask : subTasks) {
            if (subTask.statusTask.equals(Status.NEW)) {
                newSubTasksCount++;
            } else if (subTask.statusTask.equals(Status.DONE)) {
                doneSubTasksCount++;
            }
        }

        if (newSubTasksCount == subTasks.size()) {
            return Status.NEW;
        } else if (doneSubTasksCount == subTasks.size()) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }
}

