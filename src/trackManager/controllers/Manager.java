package trackManager.controllers;

import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    private Integer id = 0;

    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Epic> epicMap = new HashMap<>();
    private Map<Integer, SubTask> subTaskMap = new HashMap<>();


    public Integer createNewId() {
        id++;
        return id;
    }

// Получение списка всех задачь

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    //Удаление всех задачь

    public void deleteTasks() {
        taskMap.clear();
    }

    public void deleteEpics() {
        for (Epic epic : new ArrayList<>(epicMap.values())) {
            deleteByIdEpic(epic.id);
        }
    }

    public void deleteSubTasks() {
        for (SubTask subTask : new ArrayList<>(subTaskMap.values())) {
            deleteByIdSubTask(subTask.id);
        }
    }

    //Получение по индефикатору
    public Task getTaskById(Integer id) {
        return taskMap.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epicMap.get(id);
    }

    public SubTask getSubTaskById(Integer id) {
        return subTaskMap.get(id);
    }

    //Создание новой задачи
    public void createNewTask(Task task) {
        taskMap.put(task.id, task);
    }

    public void createNewEpic(Epic epic) {
        epicMap.put(epic.id, epic);
    }

    public void createNewSubTask(SubTask subTask) {
        subTaskMap.put(subTask.id, subTask);
    }

    //Обновление задачи

    public void updateTask(Task task) {
        taskMap.put(task.id, task);
    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.id, epic);
    }

    public void updateSubTask(SubTask subTask) {
        subTaskMap.put(subTask.id, subTask);
    }

    //Удаление по индефикатору
    public void deleteByIdTask(Integer id) {
        taskMap.remove(id);
    }


    public void deleteByIdEpic(Integer id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            List<SubTask> subTasks = epic.getSubTasks();
            for (SubTask subTask : new ArrayList<>(subTasks)) {
                deleteByIdSubTask(subTask.id);
            }
            epicMap.remove(id);

        }
    }


    public void deleteByIdSubTask(Integer id) {
        SubTask subTask = subTaskMap.get(id);

        if (subTask != null) {
            Epic epic = subTask.getEpic();

            if (epic != null) {
                epic.deleteSubTask(subTask);
            }
            subTaskMap.remove(id);

        }
    }

    //Получение списка всех подзадач определенного эпика

    public List<SubTask> getEpicAllSubtask(Epic epic) {
        return epic.getSubTasks();


    }
}






