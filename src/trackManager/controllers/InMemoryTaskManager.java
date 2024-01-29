package trackManager.controllers;

import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;


import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    private Integer id = 0;

    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, SubTask> subTaskMap = new HashMap<>();

    private final HistoryManager historyManager;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    @Override
    public Integer createNewId() {
        id++;
        return id;
    }

// Получение списка всех задачь

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    //Удаление всех задачь

    @Override
    public void deleteTasks() {
        taskMap.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : new ArrayList<>(epicMap.values())) {
            deleteByIdEpic(epic.id);
        }
    }

    @Override
    public void deleteSubTasks() {
        for (SubTask subTask : new ArrayList<>(subTaskMap.values())) {
            deleteByIdSubTask(subTask.id);
        }
    }

    //Получение по индефикатору
    @Override
    public Task getTaskById(Integer id) {
        Task task = taskMap.get(id);
//        addTaskToHistory(task);
        historyManager.add(task);

        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);

        return epic;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTask = subTaskMap.get(id);
        historyManager.add(subTask);

        return subTask;
    }

    //Создание новой задачи
    @Override
    public int createNewTask(Task task) {
        final int id = createNewId();
        task.setId(id);
        taskMap.put(id, task);
        return id;
    }

    @Override
    public int createNewEpic(Epic epic) {
        final int id = createNewId();
        epic.setId(id);
        epicMap.put(id, epic);
        return id;
    }

    @Override
    public int createNewSubTask(SubTask subTask, Epic epic) {
        final int id = createNewId();
        subTask.setId(id);
        epic.addSubTask(subTask);
        subTask.setEpic(epic);
        subTaskMap.put(id, subTask);
        return id;
    }

    //Обновление задачи

    @Override
    public void updateTask(Task task) {
        taskMap.put(task.id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.id, epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskMap.put(subTask.id, subTask);
    }

    //Удаление по индефикатору
    @Override
    public void deleteByIdTask(Integer id) {
        taskMap.remove(id);
    }


    @Override
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


    @Override
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

    @Override
    public List<SubTask> getEpicAllSubtask(Epic epic) {
        return epic.getSubTasks();


    }


    //Просмотр истории задачь

    public List<Task> getHistory() {
        return historyManager.getHistoryTasks();
    }
}