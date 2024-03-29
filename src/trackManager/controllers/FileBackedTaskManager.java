package trackManager.controllers;

import trackManager.exception.ManagerSaveException;
import trackManager.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {

    protected File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public static FileBackedTaskManager loadFormFile(File file) {

        Map<Integer, Task> restoredTasks = new HashMap<>();
        String idTaskOfHistory = "";
        boolean foundEmptyLine = false;

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
        try {
            List<String> lines = Files.lines(Path.of(file.getPath())).collect(Collectors.toList());
            if (lines.isEmpty()) {
                return new FileBackedTaskManager(new InMemoryHistoryManager(), file);
            }

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.equals("")) {
                    foundEmptyLine = true;
                }
                if (foundEmptyLine) {
                    idTaskOfHistory = line;
                } else {
                    Task task = fromString(line);
                    restoredTasks.put(task.id, task);
                }

                Collection<Task> values = restoredTasks.values();
                for (Task task : values) {
                    if (task.taskType == TaskType.TASK) {
                        fileBackedTaskManager.taskMap.put(task.getId(), task);
                    }
                    if (task.taskType == TaskType.EPIC) {
                        fileBackedTaskManager.epicMap.put(task.getId(), (Epic) task);
                    }
                    if (task.taskType == TaskType.SUBTASK) {
                        fileBackedTaskManager.subTaskMap.put(task.getId(), (SubTask) task);
                    }

                }
            }
            List<Integer> idTask = historyFromString(idTaskOfHistory);
            for (Integer id : idTask) {
                fileBackedTaskManager.getHistoryManager().add(restoredTasks.get(id));

            }

            int idMax = 0;

            for (Integer id : restoredTasks.keySet()) {
                if (idMax < id) {
                    idMax = id;
                }
            }

            fileBackedTaskManager.setId(idMax);

            for (SubTask subTask : fileBackedTaskManager.subTaskMap.values()) {
                Integer epicId = subTask.getEpicId();
                fileBackedTaskManager.epicMap.get(epicId).addSubTask(subTask);
            }


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла");
        }

        return fileBackedTaskManager;

    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {


            fileWriter.write("id,type,name,status,description,epic,startTime,duration,endTime \n");
            for (Task allTask : getAllTasks()) {
                fileWriter.write(toString(allTask) + "\n");

            }
            for (Task allTask : getAllSubtasks()) {
                fileWriter.write(toString(allTask) + "\n");

            }
            for (Task allTask : getAllEpics()) {
                fileWriter.write(toString(allTask) + "\n");

            }


            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");

        }


    }

    // переопределение создания задачи
    @Override
    public int createNewTask(Task task) {
        int taskId = super.createNewTask(task);
        save();
        return taskId;
    }


    @Override
    public int createNewEpic(Epic epic) {
        int epicID = super.createNewEpic(epic);
        save();
        return epicID;
    }

    @Override
    public int createNewSubTask(SubTask subTask, Epic epic) {
        int subTaskId = super.createNewSubTask(subTask, epic);
        save();
        return subTaskId;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }


    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task taskById = super.getTaskById(id);
        save();
        return taskById;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epicById = super.getEpicById(id);
        save();
        return epicById;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTaskById = super.getSubTaskById(id);
        save();
        return subTaskById;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteByIdTask(Integer id) {
        super.deleteByIdTask(id);
        save();
    }

    @Override
    public void deleteByIdEpic(Integer id) {
        super.deleteByIdEpic(id);
        save();
    }

    @Override
    public void deleteByIdSubTask(Integer id) {
        super.deleteByIdSubTask(id);
        save();
    }


// Переопределяю метод toString

    private static String toString(Task task) {
//        id,type,name,status,description,epic
        if (task.taskType == TaskType.SUBTASK) {
            SubTask subTask = (SubTask) task;
            return subTask.getId() + "," + subTask.getTaskType() + "," + subTask.getNameTask() + ","
                    + subTask.getStatusTask() + "," + subTask.getDescriptionTask() + "," + subTask.getEpicId() + "," +
                    subTask.getStartTime().format(subTask.getFormatter()) + "," + subTask.getDuration().toMinutes() +
                     "," + task.getEndTime();
        }
        return task.getId() + "," + task.getTaskType() + "," + task.getNameTask() + "," + task.getStatusTask() +
                "," + task.getDescriptionTask() + "," + task.getEpicId() + "," +
                task.getStartTime().format(task.getFormatter()) + "," + task.getDuration().toMinutes() +
                "," + task.getEndTime();
    }


    //создание задачи из строки
    private static Task fromString(String value) {

        // 1,TASK,имя задачиб,NEW,описани
        String[] columns = value.split(",");
        Integer id = Integer.parseInt(columns[0]);
        TaskType taskType = TaskType.valueOf(columns[1]);
        String nameTask = columns[2];
        Status statusTask = Status.valueOf(columns[3]);
        String descriptionTask = columns[4];
        Integer epicId = Integer.parseInt(columns[5]);
        LocalDateTime startTime = LocalDateTime.parse(columns[6]);
        Duration duration = Duration.ofMinutes(Integer.parseInt(columns[7]));
        LocalDateTime endTime = LocalDateTime.parse(columns[8]);
        if (taskType == TaskType.SUBTASK) {

            SubTask subTask = new SubTask(nameTask, descriptionTask, startTime, duration);
            subTask.setId(id);
            subTask.setStatusTask(statusTask);
            subTask.setTaskType(taskType);
            subTask.setEpicId(epicId);
            subTask.setStartTime(startTime);
            subTask.setDuration(duration);
            subTask.setEndTime(endTime);
            return subTask;
        }
        if (taskType == TaskType.EPIC) {
            Epic epic = new Epic(nameTask, descriptionTask, startTime, duration);
            epic.setId(id);
            epic.setStatusTask(statusTask);
            epic.setTaskType(taskType);
            epic.setStartTime(startTime);
            epic.setDuration(duration);
            epic.setEndTime(endTime);
            return epic;
        }
        Task task = new Task(nameTask, descriptionTask, startTime, duration);
        task.setId(id);
        task.setStatusTask(statusTask);
        task.setTaskType(taskType);
        task.setStartTime(startTime);
        task.setDuration(duration);
        task.setEndTime(endTime);
        return task;
    }

    // сохранение истории в CSV
    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getTasks();
        StringBuilder sb = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            sb.append(task.getId()).append(",");
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> taskId = new ArrayList<>();


        String[] id = value.split(",");
        for (String idTask : id) {
            taskId.add(Integer.parseInt(idTask));
        }


        return taskId;
    }
}