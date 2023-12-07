package trackManager.model;

import java.util.Objects;

public class Task {

    public Integer id;
    public String nameTask;
    public String descriptionTask;
    public String statusTask;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Task task = (Task) o;
//        return Objects.equals(id, task.id) && Objects.equals(nameTask, task.nameTask) && Objects.equals(descriptionTask, task.descriptionTask) && Objects.equals(statusTask, task.statusTask);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, nameTask, descriptionTask, statusTask);
//    }

    public Task(Integer id, String nameTask, String descriptionTask) {
        this.id = id;
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = "New";
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

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "trackManager.model.Task{" +
                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}