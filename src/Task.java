public class Task {

    protected Integer id;
    protected String nameTask;
    protected String descriptionTask;
    protected String statusTask;

    public Task(Integer id, String nameTask, String descriptionTask, String statusTask) {
        this.id = id;
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}
