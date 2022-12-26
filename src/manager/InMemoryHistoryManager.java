package manager;

import service.Node;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> list = new CustomLinkedList();
    private final HashMap<Integer, Node> containerLink = new HashMap<>();// дает ссылки на ноды для удаления

    @Override
    public void add(Task task) {
        Node<Task> node = new Node<>(null, task, null);
        if (containerLink.containsKey(task.getId())) {
            list.removeNode(containerLink.get(task.getId()));
        }
        list.linkLast(node);
        containerLink.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        list.removeNode(containerLink.get(id));
        containerLink.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    }

    public class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        void linkLast(Node node) {
            final Node<T> oldTail = tail;
            node.prev = oldTail;
            tail = node;
            if (oldTail == null) {
                head = node;
            } else {
                oldTail.next = node;
            }
        }

        List<Task> getTasks() {
            List<Task> tempTasks = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tempTasks.add((Task) node.task);
                node = node.next;
            }
            return tempTasks;
        }

        void removeNode(Node node) {
            Node prevNode = node.prev;
            Node nextNode = node.next;
            if (prevNode == null && nextNode == null) {
                head = null;
                tail = null;
                node = null;
            } else if (prevNode == null && nextNode != null) {
                head = nextNode;
                nextNode.prev = null;
            } else if (prevNode != null && nextNode == null) {
                tail = prevNode;
                prevNode.next = null;
            } else if (prevNode != null && nextNode != null) {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }
}