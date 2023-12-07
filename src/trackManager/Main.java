package trackManager;

import trackManager.controllers.Manager;
import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task = new Task(manager.createNewId(), "Сходить в магазин", "Купить продукты");
        Task task1 = new Task(manager.createNewId(), "Прибратся", "Разложить вещи");

        Epic epic = new Epic(manager.createNewId(), "Переез", "Подготовить вещи к переезду");
        SubTask subTask = new SubTask(manager.createNewId(), "Собрать ниги",
                "Сложить книги в коробки");
        SubTask subTask1 = new SubTask(manager.createNewId(), "Собрать одежду",
                "Сложить одежду в коробки");

        Epic epic1 = new Epic(manager.createNewId(), "Ремонт", "Поклеить обои");
        SubTask subTask2 = new SubTask(manager.createNewId(), "Купить обои", "Выбрать обои");

        manager.createNewTask(task);
        manager.createNewTask(task1);

        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.createNewSubTask(subTask1);

        manager.createNewEpic(epic1);
        manager.createNewSubTask(subTask2);

        epic.addSubTask(subTask);
        epic.addSubTask(subTask1);
        subTask.setEpic(epic);
        subTask1.setEpic(epic);

        epic1.addSubTask(subTask2);
        subTask2.setEpic(epic1);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        task.setStatusTask("Done");
        manager.updateTask(task);
        System.out.println(manager.getTaskById(1));
        task1.setStatusTask("Done");
        manager.updateTask(task1);
        System.out.println(manager.getTaskById(2));

        subTask.setStatusTask("Done");
        manager.updateSubTask(subTask);
        subTask2.setStatusTask("Done");
        manager.updateSubTask(subTask2);

        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getSubTaskById(7));

        System.out.println(epic.getStatus());
        System.out.println(epic1.getStatus());

        manager.deleteByIdTask(1);
        manager.deleteByIdEpic(3);

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));




















    }
}




