package trackManager.controllers;

import org.junit.jupiter.api.Test;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();



    @Test
    void addTest() {
        Task task = new Task("Task", "Описание задачи");
        historyManager.add(task);

        final List<Task> history = historyManager.getHistoryTasks();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}