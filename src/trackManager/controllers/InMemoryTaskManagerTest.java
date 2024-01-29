package trackManager.controllers;

import org.junit.jupiter.api.Test;

import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;

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

}
