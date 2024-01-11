package trackManager.controllers;

import trackManager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public static final int HISTORY_SIZE = 10;


    private final List<Task> historyTasks = new ArrayList<>();

    @Override
    public List<Task> getHistoryTasks() {
        return historyTasks;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            historyTasks.add(task);
        }

        if (historyTasks.size() > HISTORY_SIZE) {
            historyTasks.remove(0);
        }
    }


}
