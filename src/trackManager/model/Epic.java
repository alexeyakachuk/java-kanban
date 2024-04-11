package trackManager.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

public class Epic extends Task {

    private final List<SubTask> subTasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask, LocalDateTime startTime, Duration duration) {
        super(nameTask, descriptionTask, startTime, duration);
        this.taskType = TaskType.EPIC;
    }

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
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

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime max = LocalDateTime.MIN;
        for (SubTask subTask : subTasks) {
            LocalDateTime endTime1 = subTask.getEndTime();

            if (endTime1 != null && endTime1.compareTo(max) > 0) {
                max = endTime1;
            }
        }
        if (max.equals(LocalDateTime.MIN)) {
            return null;
        }


        return max;

    }


    public void setStartTime() {
        super.setStartTime(getStartTime());
    }

    public void setDuration() {
        super.setDuration(getDuration());
    }

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime min = LocalDateTime.MAX;
        for (SubTask subTask : subTasks) {
            LocalDateTime startTime1 = subTask.getStartTime();

            if (startTime1 != null && startTime1.compareTo(min) < 0) {
                min = startTime1;
            }

        }

        if (min.equals(LocalDateTime.MAX)) {
            return null;
        }
        return min;
    }

    @Override
    public Duration getDuration() {
        LocalDateTime startTime = getStartTime();
        LocalDateTime endTime = getEndTime();

        if(endTime == null) {
            return Duration.ofMinutes(0);
        }
        return Duration.between(startTime, endTime);
    }
}