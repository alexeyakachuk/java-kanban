package trackManager;


import trackManager.controllers.FileBackedTaskManager;
import trackManager.controllers.InMemoryTaskManager;
import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.Status;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        //TaskManager manager = Managers.getDefault();
        FileBackedTaskManager manager = FileBackedTaskManager.loadFormFile(new File("resources/Имя.csv"));
// конструктор с 4 параметрами
        Task task = new Task("task", "описание",
                LocalDateTime.of(2024, 1, 1, 11, 10, 0),
                Duration.ofMinutes(10));
        Epic epic = new Epic("epic", "писание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.of(2024, 1, 1, 9, 10, 0),
                Duration.ofMinutes(10));
        SubTask subTask1 = new SubTask("subTask", "описание",
                LocalDateTime.of(2024, 1, 1, 10, 10, 0),
                Duration.ofMinutes(20));
        int newIdTask = manager.createNewTask(task);
        int newIdEpic = manager.createNewEpic(epic);
        int newIdSubTask = manager.createNewSubTask(subTask, epic);
        int newIdSubTask1 = manager.createNewSubTask(subTask1, epic);
        manager.getSubTaskById(newIdSubTask);
        manager.getSubTaskById(newIdSubTask1);
        System.out.println(manager.getHistory());
        System.out.println(manager.getAllEpics());

        System.out.println(Managers.getGson().toJson(epic));
        System.out.println(Managers.getGson().toJson(subTask));
        System.out.println(Managers.getGson().toJson(subTask1));




// конструктор с 2 параметрами
        Task taskNew = new Task("task", "описание Task");
        Epic epicNew = new Epic("epic", "писание Epic");
        SubTask subTaskNew = new SubTask("subTask", "описание Subtask");
        SubTask subTaskNew1 = new SubTask("subTask", "описание Subtask1");
        int idTask = manager.createNewTask(taskNew);
        int idEpic = manager.createNewEpic(epicNew);
        System.out.println(manager.getAllEpics());
        int idSubTask = manager.createNewSubTask(subTaskNew, epicNew);
        int idSubTask1 = manager.createNewSubTask(subTaskNew1, epicNew);

        subTaskNew.setStartTime(LocalDateTime.of(2024,2,1,9,10,0));
        subTaskNew.setDuration(Duration.ofMinutes(10));
        manager.updateSubTask(subTaskNew);




        subTaskNew1.setStartTime(LocalDateTime.of(2024,2,1,10,10,0));
        subTaskNew1.setDuration(Duration.ofMinutes(10));
        manager.updateSubTask(subTaskNew1);
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getPrioritizedTasks());

        manager.deleteByIdSubTask(idSubTask);

        System.out.println(manager.getPrioritizedTasks());


    }
}




