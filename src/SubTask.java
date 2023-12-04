import java.util.Objects;

public class SubTask extends Task {


    private Epic epic;

    public SubTask(Integer id, String nameTask, String descriptionTask, String statusTask) {
        super(id, nameTask, descriptionTask, statusTask);
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epic, subTask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epic);
    }
}


