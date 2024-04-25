package trackManager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static trackManager.controllers.FileBackedTaskManager.parseDuration;
import static trackManager.controllers.FileBackedTaskManager.parseTime;


public class Task {


    public Integer id;
    public String nameTask;
    public String descriptionTask;
    public Status statusTask;

    public TaskType taskType;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Duration duration;



    public Task(String nameTask, String descriptionTask, LocalDateTime startTime, Duration duration) {
        this.id = 0;
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = Status.NEW;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;

    }

    public Task(String nameTask, String descriptionTask) {
        this.id = 0;
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = Status.NEW;
        this.taskType = TaskType.TASK;
        this.startTime = null;
        this.duration = null;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if(getStartTime() == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Integer getEpicId() {
        return null;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Status getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(Status statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(nameTask, task.nameTask) && Objects.equals(descriptionTask, task.descriptionTask) && statusTask == task.statusTask && taskType == task.taskType && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime) && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameTask, descriptionTask, statusTask, taskType, startTime, endTime, duration);
    }

    @Override
    public String toString() {
        return "trackManager.model.Task{" +
                "id=" + id + '\'' +
                ", type=" + taskType + '\'' +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                // ", epicId=" + null + '\'' +
                ", startTime=" + parseTime(startTime) + '\'' +
                "duration=" + parseDuration(duration) + '\'' +
                "endTime=" + endTime + '\'' +
                '}';
    }




}
