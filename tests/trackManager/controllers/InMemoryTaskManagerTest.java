package trackManager.controllers;

import org.junit.jupiter.api.Test;

import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void createNewTaskTest() {

        Task task = new Task("Прогулка", "Пойти гулять");

        final int taskId = manager.createNewTask(task);
        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");


    }

    @Test
    void createNewEpicTest() {

        Epic epic = new Epic("Прогулка", "Пойти гулять");

        final int epicId = manager.createNewEpic(epic);
        final Task savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");


    }

    @Test
    void createNewSubTaskTest() {

        Epic epic = new Epic("Epic", "Описание эпика");
        SubTask subTask = new SubTask("SubTask", "Описание сабтаски");

        final int subTaskId = manager.createNewSubTask(subTask, epic);
        final SubTask savedSubTask = manager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

    }

    @Test
    void deleteByIdSubTaskTest() {
        Epic epic = new Epic("Epic", "Описание эпика");
        SubTask subTask = new SubTask("SubTask", "Описание сабтаски");
        final int epicId = manager.createNewEpic(epic);
        final int subTaskId = manager.createNewSubTask(subTask, epic);

        List<SubTask> allSubtasks = manager.getAllSubTasks();
        assertEquals(1, allSubtasks.size());
        manager.deleteByIdSubTask(subTaskId);
        allSubtasks = manager.getAllSubTasks();
        assertEquals(0, allSubtasks.size());
    }

    @Test
    void deleteByIdEpicTest() {
        Epic epic = new Epic("Epic", "Описание эпика");
        final int newEpicID = manager.createNewEpic(epic);
        SubTask subTask = new SubTask("SubTask", "Описание сабтаски");
        final int subTaskId = manager.createNewSubTask(subTask, epic);


        List<Epic> allEpics = manager.getAllEpics();
        assertEquals(1, allEpics.size());
        List<SubTask> allSubtasks = manager.getAllSubTasks();
        assertEquals(1, allSubtasks.size());

        manager.deleteByIdEpic(newEpicID);

        allSubtasks = manager.getAllSubTasks();
        assertEquals(0, allSubtasks.size());
        allEpics = manager.getAllEpics();
        assertEquals(0, allEpics.size());
    }

}
