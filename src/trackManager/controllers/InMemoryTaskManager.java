package trackManager.controllers;

import trackManager.exception.ManagerValidateException;
import trackManager.exception.NotFoundException;
import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.model.Task;
import trackManager.utils.TaskComparator;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private Integer id = 0;

    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();
    protected final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskComparator());

    private final HistoryManager historyManager;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    // добавил геттер и сеттер id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<SubTask> getAllSubTasks() {
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
        if (task == null) {
            throw new NotFoundException("Задча с id " + id + " не найдена");
        }
        historyManager.add(task);

        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epicMap.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпик с id " + id + " не найден");
        }
        historyManager.add(epic);

        return epic;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTask = subTaskMap.get(id);
        if (subTask == null) {
            throw new NotFoundException("Подзадача с id " + id + " не найден");
        }
        historyManager.add(subTask);

        return subTask;
    }

    //Создание новой задачи
    @Override
    public int createNewTask(Task task) {
        addToPrioritizedTasks(task);
        final int id = createNewId();
        task.setId(id);
        taskMap.put(id, task);

        return id;
    }



    @Override
    public int createNewEpic(Epic epic) {
        addToPrioritizedTasks(epic);
        final int id = createNewId();
        epic.setId(id);
        epicMap.put(id, epic);

        return id;
    }

    @Override
    public int createNewSubTask(SubTask subTask, Epic epic) {
        addToPrioritizedTasks(subTask);
        final int id = createNewId();
        subTask.setId(id);
        epic.addSubTask(subTask);
        subTask.setEpicId(epic.getId());
        subTaskMap.put(id, subTask);

        return id;
    }

    private void addToPrioritizedTasks(Task task) {
        Integer idTask = task.getId();
        prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.getId().equals(idTask));

        if (task.getStartTime() != null) {
            if (isTimeFree(task)) {
                prioritizedTasks.add(task);
            } else {
                throw new ManagerValidateException("Задача пересекается с другой задачей!");
            }
        }
    }

    //Обновление задачи

    @Override
    public void updateTask(Task task) {
        taskMap.put(task.id, task);
        addToPrioritizedTasks(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.id, epic);

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskMap.put(subTask.id, subTask);
        addToPrioritizedTasks(subTask);


    }

    //Удаление по индефикатору
    @Override
    public void deleteByIdTask(Integer id) {
        Task task = taskMap.get(id);
        taskMap.remove(id);
        historyManager.removeTask(id);
        prioritizedTasks.remove(task);
    }


    @Override
    public void deleteByIdEpic(Integer id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            epic.getSubTasks().forEach(subTask -> deleteByIdSubTask(subTask.id));
            epicMap.remove(id);
            historyManager.removeTask(id);
            prioritizedTasks.remove(epic);

        }
    }


    @Override
    public void deleteByIdSubTask(Integer id) {
        SubTask subTask = subTaskMap.get(id);

        if (subTask != null) {
            Integer epicId = subTask.getEpicId();

            if (epicId != null) {
                Epic epic = epicMap.get(epicId);
                epic.deleteSubTask(subTask);
            }

            subTaskMap.remove(id);
            historyManager.removeTask(id);
            prioritizedTasks.remove(subTask);

        }
    }

    //Получение списка всех подзадач определенного эпика

    @Override
    public List<SubTask> getEpicAllSubtask(Epic epic) {
        return epic.getSubTasks();


    }


    //Просмотр истории задачь

    public List<Task> getHistory() {
        return historyManager.getTasks();
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Проверка на пересечение времени задач
    public boolean isTimeOverlapping(Task first, Task second) {
        if(first.getStartTime() == null || first.getEndTime() == null ||
                second.getStartTime() == null || second.getEndTime() == null){
            return false;
        }

        return ((first.getStartTime().isBefore(second.getEndTime()) && first.getEndTime().isAfter(second.getStartTime())) ||
                (second.getStartTime().isBefore(first.getEndTime()) && second.getEndTime().isAfter(first.getStartTime())));

    }


//

    private boolean isTimeFree(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return true;
        }
        return prioritizedTasks.stream()
                .noneMatch(t -> isTimeOverlapping(task, t));
    }

}
