package trackManager.controllers;

import org.junit.jupiter.api.Test;
import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();

    @Test
    void addTest() {
        Task task = new Task("Task", "Описание задачи");

        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getTasks();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        System.out.println(historyManager.getTasks());
    }

    @Test
    void removeTaskTest(){

        Task task = new Task("Task", "Описание Задачи");
        Epic epic = new Epic("Epic", "Описание задачи");
        SubTask subTask = new SubTask("SubTask", "Описание задачи");
        SubTask subTask1 = new SubTask("SubTask1", "Описание задачи");

        manager.createNewTask(task);
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        manager.createNewSubTask(subTask1, epic);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.add(subTask);
        historyManager.add(subTask1);
        System.out.println(historyManager.getTasks());

        List<Task> history = historyManager.getTasks();

        assertNotNull(history, "История не пустая.");
        assertEquals(4, history.size(), "История не пустая.");

        historyManager.removeTask(subTask.id);
        history = historyManager.getTasks();
        assertEquals(3, history.size());

        historyManager.removeTask(task.id);
        history = historyManager.getTasks();
        assertEquals(2, history.size());


    }


}