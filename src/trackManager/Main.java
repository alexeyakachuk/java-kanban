package trackManager;

import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.Status;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.Managers;


public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();


        Task task = new Task("Сходить в магазин", "Купить продукты");
        Task task1 = new Task("Прибратся", "Разложить вещи");

        Epic epic = new Epic("Переезд", "Подготовить вещи к переезду");
        SubTask subTask = new SubTask("Собрать ниги",
                "Сложить книги в коробки");
        SubTask subTask1 = new SubTask("Собрать одежду",
                "Сложить одежду в коробки");

        Epic epic1 = new Epic("Ремонт", "Поклеить обои");
        SubTask subTask2 = new SubTask("Купить обои", "Выбрать обои");


        manager.createNewTask(task);
        manager.createNewTask(task1);

        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask, epic);
        manager.createNewSubTask(subTask1, epic);

        manager.createNewEpic(epic1);
        manager.createNewSubTask(subTask2, epic1);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        task.setStatusTask(Status.DONE);
        manager.updateTask(task);
        System.out.println(manager.getTaskById(1));
        task1.setStatusTask(Status.DONE);
        manager.updateTask(task1);
        System.out.println(manager.getTaskById(2));

        subTask.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask);
        subTask2.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask2);


        System.out.println(epic.getStatus());
        System.out.println(epic1.getStatus());




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



        System.out.println(manager.getHistory());

        manager.deleteByIdTask(1);
        manager.deleteByIdEpic(3);

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));


    }
}




