package trackManager.controllers;

import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;
import java.util.List;


public interface TaskManager {




    public Integer createNewId();

// Получение списка всех задачь

    public List<Task> getAllTasks();

    public List<Epic> getAllEpics();

    public List<SubTask> getAllSubtasks();

    //Удаление всех задачь

    public void deleteTasks();

    public void deleteEpics();

    public void deleteSubTasks();

    //Получение по индефикатору
    public Task getTaskById(Integer id);

    public Epic getEpicById(Integer id);

    public SubTask getSubTaskById(Integer id);

    //Создание новой задачи
    public void createNewTask(Task task);

    public void createNewEpic(Epic epic);

    public void createNewSubTask(SubTask subTask, Epic epic);

    //Обновление задачи

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubTask(SubTask subTask);

    //Удаление по индефикатору
    public void deleteByIdTask(Integer id);


    public void deleteByIdEpic(Integer id);


    public void deleteByIdSubTask(Integer id);

    //Получение списка всех подзадач определенного эпика

    public List<SubTask> getEpicAllSubtask(Epic epic);


    // Просмотр истории задачь
    public List<Task> getHistory();
}






