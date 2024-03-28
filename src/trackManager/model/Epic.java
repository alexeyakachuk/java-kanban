package trackManager.model;

//import javax.xml.datatype.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

public class Epic extends Task {

    private final List<SubTask> subTasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask, LocalDateTime startTime,Duration duration) {
        super(nameTask, descriptionTask, startTime, duration);
        this.taskType = TaskType.EPIC;
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
    @Override
    public Status getStatusTask() {
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