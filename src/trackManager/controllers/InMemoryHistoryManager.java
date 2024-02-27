package trackManager.controllers;


import trackManager.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {





    private final Map<Integer, Node<Task>> map = new HashMap<>();

    public Node<Task> head = null;

    public Node<Task> tail = null;



    @Override
    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node<Task> fakeHead = head;

        while (fakeHead != null) {
            result.add(fakeHead.data);
            fakeHead = fakeHead.next;
        }

        return result;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Node<Task> taskNode = map.get(task.id);
            if(taskNode != null) {
                removeNode(taskNode);
            }
            linkLast(task);
        }


    }

    @Override
    public void removeTask(Integer id) {

        Node<Task> taskNode = map.get(id);
        removeNode(taskNode);

    }

//    @Override
    private void linkLast(Task task) {

        Node<Task> node = new Node<>(task);

        if(head == null) {
            head = node;
        }
        node.prev = tail;
        if(node.prev != null) {
            node.prev.next = node;
        }

        tail = node;

        map.put(task.id, node);

    }
//    @Override
    private void removeNode(Node<Task> node) {
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;

        //Если удаляем из середины, то просто меняем ссылки
        if(prev != null && next != null){
            prev.next = next;
            next.prev = prev;
        }

        //Если удаляем head

        if(prev == null && next == null) {
           head = null;
           tail = null;
        }


        if (prev == null && next != null) {
            head = next;
        }

        //Если удаляем tail
        if (prev != null && next == null) {
            tail = prev;
        }

        map.remove(node.data.id);
    }
    private static class Node <T> {

        public T data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}
