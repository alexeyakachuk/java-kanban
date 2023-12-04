import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    Integer id = 0;

    Map<Integer, Task> taskMap = new HashMap<>();
    Map<Integer, Epic> epicMap = new HashMap<>();
    Map<Integer, SubTask> subTaskMap = new HashMap<>();


    public Integer createNewId() {
        id++;
        return id;
    }

// Получение списка всех задачь

    public Map<Integer, Task> getAllTasks() {
        return taskMap;
    }

    public Map<Integer, Epic> getAllEpics() {
        return epicMap;
    }

    public Map<Integer, SubTask> getAllSubtasks() {
        return subTaskMap;
    }

    //Удаление всех задачь

    public void deleteTasks() {
        taskMap.clear();
    }

    public void deleteEpics() {
        epicMap.clear();
    }

    public void deleteSubTasks() {
        subTaskMap.clear();
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
        // Task newTask = new Task(task.id, task.nameTask, task.descriptionTask, task.statusTask);
        taskMap.put(task.id, task);
    }

    public void createNewEpic(Epic epic) {
        // Epic newEpic = new Epic(epic.id, epic.nameTask, epic.descriptionTask, epic.statusTask);
        epicMap.put(epic.id, epic);
    }

    public void createNewSubTask(SubTask subTask) {
        // SubTask newSubTask = new SubTask(subTask.id, subTask.nameTask, subTask.descriptionTask, subTask.statusTask);
        subTaskMap.put(subTask.id, subTask);
    }

    //Обновление задачи

    public void updateTask(Task task) {
//        Task updateTask = new Task(task.id, task.nameTask, task.descriptionTask, task.statusTask);
        taskMap.put(task.id, task);
    }

    public void updateEpic(Epic epic) {
//        Epic updateEpic = new Epic(epic.id, epic.nameTask, epic.descriptionTask, epic.statusTask);
        epicMap.put(epic.id, epic);
    }

    public void updateSubTask(SubTask subTask) {
//        SubTask updateSubTask = new SubTask(subTask.id, subTask.nameTask, subTask.descriptionTask, subTask.statusTask);
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
