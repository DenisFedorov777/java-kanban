package main.manager;

import main.service.Node;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> list = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        Node<Task> node = new Node<>(null, task, null);
        int taskId = task.getId();
        if (list.containerLink.containsKey(taskId)) {
            list.removeNode(list.containerLink.get(taskId));
        }
        list.linkLast(node);
        list.containerLink.put(taskId, list.getTail());
    }

    @Override
    public void remove(int id) {
        if(list.containerLink.get(id) != null) {
            list.removeNode(list.containerLink.get(id));
            list.containerLink.remove(id);
        } else {
            System.out.println("Такой ноды не существует");
        }
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    }

    public class CustomLinkedList<T> {
        HashMap<Integer, Node<Task>> containerLink = new HashMap<>();
        private Node<T> head;
        private Node<T> tail;

        public Node<T> getTail() {
            return tail;
        }

        void linkLast(Node<T> node) {
            final Node<T> oldTail = tail;
            node.prev = oldTail;
            tail = node;
            if (oldTail == null) {
                head = node;
            } else {
                oldTail.next = node;
            }
        }
        List<T> getTasks() {
            List<T> tempTasks = new ArrayList<>();
            Node<T> node = head;
            while (node != null) {
                tempTasks.add( node.data);
                node = node.next;
            }
            return tempTasks;
        }

        void removeNode(Node<T> node) {
            Node<T> prevNode = node.prev;
            Node<T> nextNode = node.next;
            if (prevNode == null && nextNode == null) {
                head = null;
                tail = null;
            } else if (prevNode == null && nextNode != null) {
                head = nextNode;
                nextNode.prev = null;
            } else if (prevNode != null && nextNode == null) {
                tail = prevNode;
                prevNode.next = null;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }
}