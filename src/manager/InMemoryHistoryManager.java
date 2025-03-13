package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final TaskLinkedList<Task> history;
    private final Map<Integer, Node<Task>> historyMap;

    public InMemoryHistoryManager() {
        history = new TaskLinkedList<>();
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



