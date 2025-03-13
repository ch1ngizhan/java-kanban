package Test;

import manager.HistoryManager;
import manager.Managers;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryHistoryManagerTest {
    @Test
    void testAddToHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task1.setId(1);
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи.");
        assertEquals(task1, history.get(0), "Первая задача в истории не совпадает.");
        assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает.");
    }

    @Test
    void testRemoveFromHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task1.setId(1);
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать 1 задачу после удаления.");
        assertEquals(task2, history.get(0), "Оставшаяся задача в истории не совпадает.");
    }

    @Test
    void testHistoryLimit() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        for (int i = 1; i <= 11; i++) {
            Task task = new Task("Task " + i, "Description " + i, Status.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История должна содержать не более 10 задач.");
        assertFalse(history.contains(new Task("Task 1", "Description 1", Status.NEW)),
                "Первая задача должна быть удалена из истории.");
    }

    @Test
    void testRemoveNode() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Task task3 = new Task("Task 3", "Description 3", Status.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи после удаления.");
        assertEquals(task1, history.get(0), "Первая задача в истории не совпадает.");
        assertEquals(task3, history.get(1), "Третья задача в истории не совпадает.");
    }
}

