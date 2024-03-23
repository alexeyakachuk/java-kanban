package trackManager.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackManager.model.Epic;
import trackManager.model.Status;
import trackManager.model.SubTask;
import trackManager.model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    File file;

    @BeforeEach
    void saveTest() throws IOException {
        file = File.createTempFile("test", ".csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFormFile(file);

        // When
        Task task1 = new Task("Task 1", "описание");
        fileBackedTaskManager.createNewTask(task1);


        Epic epic1 = new Epic("Epic 1", "описание");
        fileBackedTaskManager.createNewEpic(epic1);


        SubTask subTask1 = new SubTask("SubTask 1", "описание");
        fileBackedTaskManager.createNewSubTask(subTask1, epic1);


        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getEpicById(2);
        fileBackedTaskManager.getSubTaskById(3);
    }




    @Test
    void loadAndSaveFromFileTest() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFormFile(file);

        List<Task> allTasks = fileBackedTaskManager.getAllTasks();
        List<Epic> allEpics = fileBackedTaskManager.getAllEpics();
        List<SubTask> allSubtasks = fileBackedTaskManager.getAllSubtasks();
        List<Task> history = fileBackedTaskManager.getHistory();

        assertEquals(1, allTasks.size());
        assertEquals(1, allEpics.size());
        assertEquals(1, allSubtasks.size());
        assertEquals(3, history.size());

    }

    @Test
    void updateTest() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFormFile(file);
        Task taskById = fileBackedTaskManager.getTaskById(1);
        taskById.setStatusTask(Status.DONE);
        fileBackedTaskManager.updateTask(taskById);
        Status statusTask = taskById.getStatusTask();
        assertEquals(statusTask, Status.DONE);

        SubTask subTaskById = fileBackedTaskManager.getSubTaskById(3);
        subTaskById.setStatusTask(Status.IN_PROGRESS);
        fileBackedTaskManager.updateSubTask(subTaskById);
        Status statusSubTask = subTaskById.getStatusTask();
        assertEquals(statusSubTask, Status.IN_PROGRESS);

        Epic epicById = fileBackedTaskManager.getEpicById(2);
        Status statusEpic = epicById.getStatusTask();
        assertEquals(statusEpic, Status.IN_PROGRESS);


    }


    @Test
    void deleteTasksTest() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFormFile(file);

        fileBackedTaskManager.deleteByIdTask(1);
        List<Task> allTasks = fileBackedTaskManager.getAllTasks();
        assertEquals(0, allTasks.size());

        fileBackedTaskManager.deleteByIdSubTask(3);
        List<SubTask> allSubtasks = fileBackedTaskManager.getAllSubtasks();
        assertEquals(0, allSubtasks.size());

        fileBackedTaskManager.deleteByIdEpic(2);
        List<Epic> allEpics = fileBackedTaskManager.getAllEpics();
        System.out.println(allEpics);
        assertEquals(0, allEpics.size());

        List<Task> history = fileBackedTaskManager.getHistory();
        System.out.println(history);
        assertEquals(0, history.size());
    }
}

