package trackManager.controllers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;



public class FileBackedTaskManagerTest {
    TaskManager manager = Managers.getDefault();
    private File tempFile;

    //@AfterEach

    @Test
    void testSaveEmptyFileTest() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(manager.getHistoryManager(),
                tempFile);


        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write("id,type,name,status,description,epic \n");
        fileWriter.write("10,TASK,Прибратся,DONE,Разложить вещи");

        fileWriter.close();

        fileBackedTaskManager.loadFromFile();

        List<Task> allTasks = fileBackedTaskManager.getAllTasks();
        System.out.println(allTasks);

        Assertions.assertEquals(1, allTasks.size());
    }



    @Test
    void deleteTasksTest() throws IOException {
        tempFile = File.createTempFile("test", "csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(manager.getHistoryManager(),
                tempFile);

        Task task = new Task("Task", "Описание");
        fileBackedTaskManager.createNewTask(task);
        Epic epic = new Epic("Epic", "Описание");
        fileBackedTaskManager.createNewEpic(epic);
        SubTask subTask = new SubTask("SubTask", "Описание");
        fileBackedTaskManager.createNewSubTask(subTask, epic);

        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getEpicById(2);


        fileBackedTaskManager.deleteByIdEpic(2);

        fileBackedTaskManager.loadFromFile();
        List<Task> history = fileBackedTaskManager.getHistory();
        Assertions.assertNotNull( history);

        fileBackedTaskManager.getAllSubtasks();
        List<Epic> allEpics = fileBackedTaskManager.getAllEpics();
        Assertions.assertEquals(0, allEpics.size());


    }
}
