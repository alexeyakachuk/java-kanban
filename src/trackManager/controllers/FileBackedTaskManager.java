package trackManager.controllers;

import trackManager.exception.ManagerSaveException;
import trackManager.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic \n");
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

        if (task.taskType == TaskType.SUBTASK) {
            SubTask subTask = (SubTask) task;
            return subTask.getId() + "," + subTask.taskType + "," + subTask.getNameTask() + "," + subTask.getStatusTask() +
                    "," + subTask.getDescriptionTask() + "," + subTask.getEpicId();
        }

        return task.getId() + "," + task.taskType + "," + task.getNameTask() + "," + task.getStatusTask() +
                "," + task.getDescriptionTask();
    }



    //создание задачи из строки
    private static Task fromString(String value) {

        // 1,TASK,имя задачиб,NEW,описани
        String[] columns = value.split(",");

        Integer id = Integer.parseInt(columns[0]);
        TaskType typeTask = TaskType.valueOf(columns[1]);
        String nameTask = columns[2];
        Status statusTask = Status.valueOf(columns[3]);
        String descriptionTask = columns[4];

        if (typeTask == TaskType.SUBTASK) {
            Integer epicId = Integer.parseInt(columns[5]);

            SubTask subTask = new SubTask(nameTask, descriptionTask);
            subTask.setId(id);
            subTask.setStatusTask(statusTask);
            subTask.setEpicId(epicId);
            return subTask;
        }

        if (typeTask == TaskType.EPIC) {
            Epic epic = new Epic(nameTask, descriptionTask);
            epic.setId(id);
            epic.setStatusTask(statusTask);
            return epic;
        }

        Task task = new Task(nameTask, descriptionTask);
        task.setId(id);
        task.setStatusTask(statusTask);

        return task;
    }


    public static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getTasks()) {
            sb.append((toString(task))).append("\n");

        }
        return sb.toString();
    }

    public static List<Task> historyFromString(List<String> history) {
        List<Task> tasks = new ArrayList<>();
        for (String column : history) {
            tasks.add(fromString(column));
        }

        return tasks;
    }

    public void loadFromFile() {
        List<Task> tasks = new ArrayList<>();
        List<Task> historyTask;

        try {
            List<String> lines = Files.lines(Path.of(file.getPath())).collect(Collectors.toList());

            int indexEmptyLine = lines.size();
            boolean historyExist = lines.contains("");
            if(historyExist) {
                indexEmptyLine = lines.indexOf("");
            }

            for (int i = 1; i < indexEmptyLine; i++) {
                String line = lines.get(i);

                Task task = fromString(line);
                tasks.add(task);

            }
            for (Task task : tasks) {
                if (task.taskType == TaskType.TASK) {
                    taskMap.put(task.id, task);
                }
                if (task.taskType == TaskType.EPIC) {
                    epicMap.put(task.id, (Epic) task);
                }
                if (task.taskType == TaskType.SUBTASK) {
                    subTaskMap.put(task.id, (SubTask) task);
                }

            }

            if(!historyExist) {
                return;
            }

            historyTask = historyFromString(lines.subList(indexEmptyLine + 1, lines.size() - 1));


            for (Task task : historyTask) {
                getHistoryManager().add(task);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла");
        }
    }
}
