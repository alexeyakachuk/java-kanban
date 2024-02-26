package trackManager.controllers;

import trackManager.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getTasks();

    void removeTask(Integer id);

//    void linkLast(Task task);
//
//    void removeNode(Node<Task> node);


}
