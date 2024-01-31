package trackManager.controllers;

import trackManager.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistoryTasks();
}
