package trackManager.model;



import java.util.Objects;

public class SubTask extends Task {




    private Integer epicId;

    public SubTask(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
        this.taskType = TaskType.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;

    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicId, subTask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +

                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask=" + statusTask +
                ", taskType=" + taskType +
                ", epicId=" + epicId +
                '}';
    }
}



