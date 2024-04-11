package trackManager.controllers;

import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;

import java.util.List;


public interface TaskManager {


    Integer createNewId();

// Получение списка всех задачь

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    //Удаление всех задачь

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    //Получение по индефикатору
    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    SubTask getSubTaskById(Integer id);

    //Создание новой задачи
    int createNewTask(Task task);

    int createNewEpic(Epic epic);

    int createNewSubTask(SubTask subTask, Epic epic);

    //Обновление задачи

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    //Удаление по индефикатору
    void deleteByIdTask(Integer id);


    void deleteByIdEpic(Integer id);


    void deleteByIdSubTask(Integer id);

    //Получение списка всех подзадач определенного эпика

    List<SubTask> getEpicAllSubtask(Epic epic);


    // Просмотр истории задачь
    List<Task> getHistory();

    HistoryManager getHistoryManager();


    boolean isTimeOverlapping(Task first, Task second);
}
