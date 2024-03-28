package trackManager;


import trackManager.controllers.FileBackedTaskManager;
import trackManager.controllers.InMemoryHistoryManager;
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

//        manager.loadFromFile();
//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getHistory());
//        System.out.println(manager.getSubTaskById(7));
//        System.out.println(manager.getHistory());
//        System.out.println(manager.getAllTasks());
        //FileBackedTaskManager.loadFormFile(new File("resources/Имя.csv"));


//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getAllEpics());
//        System.out.println(manager.getAllSubtasks());
//
//        System.out.println(manager.getHistory());
////
        Task task = new Task("задача 1", "описание",
                LocalDateTime.of(2024,3,27, 14,9,0), Duration.ofMinutes(15));
//        int taskId = manager.createNewTask(task);
//        manager.getTaskById(taskId);
//
//
//        Epic epic = new Epic("epic 1", "описание",
//                LocalDateTime.of(2023,10,10, 4,23), Duration.ofMinutes(20));
//        int epicId = manager.createNewEpic(epic);
//        manager.getEpicById(epicId);
//        SubTask subTask = new SubTask("subTask", "описание",
//                LocalDateTime.of(2023,11,10, 8,45), Duration.ofMinutes(15));
//        int subTaskId = manager.createNewSubTask(subTask, epic);
//        manager.getSubTaskById(subTaskId);
//
//        System.out.println(manager.getHistory());

//        Task task = new Task("Сходить в магазин", "Купить продукты",
//                LocalDateTime(2024,10,6,23,45), Duration.ofMinutes(10));
        Task task1 = new Task("Прибратся", "Разложить вещи",
                LocalDateTime.of(2024,10,6,23,45), Duration.ofMinutes(10));
//
        Epic epic = new Epic("Переезд", "Подготовить вещи к переезду",
                LocalDateTime.of(2024,3,16,16,13), Duration.ofMinutes(7));
        SubTask subTask = new SubTask("Собрать ниги", "Сложить книги в коробки",
                LocalDateTime.of(2024,4,7,17,40), Duration.ofMinutes(10));
        SubTask subTask1 = new SubTask("Собрать одежду", "Сложить одежду в коробки",
                LocalDateTime.of(2024,6,20,20,30), Duration.ofMinutes(30));

        Epic epic1 = new Epic("Ремонт", "Поклеить обои",
                LocalDateTime.of(2024,9,25,10,15), Duration.ofMinutes(5));
        SubTask subTask2 = new SubTask("Купить обои", "Выбрать обои",
                LocalDateTime.of(2024,10,17,9,45), Duration.ofMinutes(10));
//
//
        manager.createNewTask(task);
        manager.createNewTask(task1);

        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        manager.createNewSubTask(subTask1, epic);

        manager.createNewEpic(epic1);
        manager.createNewSubTask(subTask2, epic1);
//
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        task.setStatusTask(Status.DONE);
        manager.updateTask(task);

        task1.setStatusTask(Status.DONE);
        manager.updateTask(task1);


        subTask.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask);
        subTask2.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask2);


//        System.out.println(epic.getStatus());
//        System.out.println(epic1.getStatus());


        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getEpicById(6));
        System.out.println(manager.getSubTaskById(7));
        System.out.println(manager.getSubTaskById(7));
        System.out.println(manager.getSubTaskById(7));
        System.out.println(manager.getSubTaskById(7));
        System.out.println(manager.getTaskById(1));
//        System.out.println(epic1.getStatus());

        System.out.println(manager.getHistory());

        manager.deleteByIdTask(1);
        manager.deleteByIdSubTask(4);
        manager.deleteByIdEpic(3);


        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));

        // manager.getHistoryManager().removeTask(5);


        System.out.println("История " + manager.getHistory());


        System.out.println("Все задачи " + manager.getAllTasks());

        System.out.println(manager.getAllTasks());

        System.out.println(manager.getHistory());

        System.out.println(manager.getHistoryManager());


    }
}




