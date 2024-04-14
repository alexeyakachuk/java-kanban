package trackManager.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackManager.exception.ManagerValidateException;
import trackManager.model.Epic;
import trackManager.model.Status;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {

    protected TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();

    }

    @Test
    void createNewTaskTest() {

        Task task = new Task("task", "описание",
                LocalDateTime.of(2024, 1, 1, 10, 10, 0), Duration.ofMinutes(10));

        final int taskId = manager.createNewTask(task);
        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

    }

    @Test
    void createNewEpicTest() {

        Epic epic = new Epic("epic", "описание",
                null, null);

        final int epicId = manager.createNewEpic(epic);
        final Task savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");


    }

    @Test
    void createNewSubTaskTest() {

        Epic epic = new Epic("Epic", "описание");
        SubTask subTask = new SubTask("SubTask", "Описание сабтаски",
                LocalDateTime.of(2024,1,1,10,10,0), Duration.ofMinutes(10));

        final int subTaskId = manager.createNewSubTask(subTask, epic);
        final SubTask savedSubTask = manager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

    }

    @Test
    void startTimeEpicTest() {
        Epic epic = new Epic("Epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.of(2024,1,1,10,10,0), Duration.ofMinutes(10));
        int newEpic = manager.createNewEpic(epic);
        int newSubTask = manager.createNewSubTask(subTask, epic);

        LocalDateTime startTime = epic.getStartTime();
        LocalDateTime startTime1 = subTask.getStartTime();

        assertEquals(startTime, startTime1);
    }

    @Test
    void endTimeEpicTest() {
        Epic epic = new Epic("Epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.of(2024,1,1,10,10,0), Duration.ofMinutes(10));

        int newEpic = manager.createNewEpic(epic);
        int newSubTask = manager.createNewSubTask(subTask, epic);
        subTask.setStatusTask(Status.DONE);
        LocalDateTime endTime = epic.getEndTime();
        LocalDateTime endTime1 = subTask.getEndTime();

        assertEquals(endTime, endTime1);
    }

    @Test
    void updateStarTime() {
        Task task = new Task("task", "описание");
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание");

        manager.createNewTask(task);
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        task.setStartTime(LocalDateTime.of(2024,1,1,10,10,0));
        task.setDuration(Duration.ofMinutes(10));
        manager.updateTask(task);
        subTask.setStartTime(LocalDateTime.of(2024,1,2,10,10,0));
        subTask.setDuration(Duration.ofMinutes(10));
        manager.updateSubTask(subTask);

        LocalDateTime startTimeTask = task.getStartTime();
        LocalDateTime startTimeEpic = epic.getStartTime();
        LocalDateTime startTimeSybTask = subTask.getStartTime();

        assertEquals(startTimeTask, LocalDateTime.of(2024,1,1,10,10,0));


    }

    @Test
    void isTimeFreeDoneSubTaskTest() {
        // создаем подзадачу, которая не пересекается с другими
        SubTask subTask1 = new SubTask("subTask1", "описание",
                LocalDateTime.of(2023, 3, 20, 10, 0), Duration.ofMinutes(10));

        // создаем подзадачу, которая пересекается с первой
        SubTask subTask2 = new SubTask("subTask2", "описание",
                LocalDateTime.of(2023, 3, 20, 11, 0), Duration.ofMinutes(10));

        SubTask subTask3 = new SubTask("subTask3", "описание",
                LocalDateTime.of(2023,3,20,11,0), Duration.ofMinutes(20));

        Epic epic = new Epic("epic", "описание");
        manager.createNewEpic(epic);

        // добавляем первую подзадачу в менеджер
        manager.createNewSubTask(subTask1, epic);
        manager.createNewSubTask(subTask2, epic);


        //  проверяем, что вторая подзадача не пересекается с первой
        assertFalse(manager.isTimeOverlapping(subTask1, subTask2));
        //  проверяем, что вторая подзадача  пересекается с третей
        ManagerValidateException thrown = assertThrows(
                ManagerValidateException.class,
                () -> manager.createNewSubTask(subTask3, epic),
                "Задачи не пересекаются"
        );

        assertTrue(thrown.getMessage().contains("Задача пересекается с другой задачей!"));
        assertFalse(manager.isTimeOverlapping(subTask2, subTask1));
    }

}


