package trackManager.model;

import org.junit.jupiter.api.Test;

import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.Status;
import trackManager.model.SubTask;
import trackManager.utils.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;



class EpicTest {

    TaskManager manager = Managers.getDefault();

    @Test
    void getStatusNewWithEmptySubTaskListTest() {
        Epic epic = new Epic("Epic", "Описание epic");
        manager.createNewEpic(epic);

        Status status = epic.getStatus();

        assertEquals(status, Status.NEW);
    }


    @Test
    void getStatusNewTest() {
        Epic epic = new Epic("Epic", "Описание epic");
        SubTask subTask = new SubTask("SubTask", "Описание SubTask");
        SubTask subTask1 = new SubTask("SubTask1", "Описание SubTask1");
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        manager.createNewSubTask(subTask1, epic);

        Status status = epic.getStatus();

        assertEquals(status, Status.NEW);
    }

    @Test
    void getStatusDoneTest() {
        Epic epic = new Epic("Epic", "Описание epic");
        SubTask subTask = new SubTask("SubTask", "Описание SubTask");
        SubTask subTask1 = new SubTask("SubTask1", "Описание SubTask1");
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        manager.createNewSubTask(subTask1, epic);
        subTask.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask);
        subTask1.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask1);

        Status status = epic.getStatus();

        assertEquals(status, Status.DONE);
    }

    @Test
    void getStatusInProgressTest() {
        Epic epic = new Epic("Epic", "Описание epic");
        SubTask subTask = new SubTask("SubTask", "Описание SubTask");
        SubTask subTask1 = new SubTask("SubTask1", "Описание SubTask1");
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        manager.createNewSubTask(subTask1, epic);
        subTask.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask);

        Status status = epic.getStatus();

        assertEquals(status, Status.IN_PROGRESS);
    }
}