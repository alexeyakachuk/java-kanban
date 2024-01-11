package trackManager.controllers;

import trackManager.model.Task;

import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistoryTasks();
}
