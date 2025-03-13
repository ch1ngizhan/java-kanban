package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final taskLinkedList<Task> history;
    private final Map<Integer, Node<Task>> historyMap;

    public InMemoryHistoryManager() {
        history = new taskLinkedList<>();
        historyMap = new HashMap<>();
    }


    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.getTasks());
    }


    @Override
    public void add(Task task) {
        if (historyMap.size() == 10) {
            int removeID = history.head.data.getId();
            remove(removeID);
        }
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        historyMap.put(task.getId(), history.linkLast(task));
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            history.head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            history.tail = node.prev;
        }
    }
}


class taskLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;


    taskLinkedList() {
        head = null;
        tail = null;
    }

    Node<T> linkLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;

        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;


        }
        return newNode;
    }

    List<T> getTasks() {
        Node<T> task = head;
        List<T> result = new ArrayList<>();
        while (task != null) {
            result.add(task.data);
            task = task.next;
        }
        return result;

    }
}