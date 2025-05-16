import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add_ShouldAddTaskToHistory() {
        Task task = new Task("Test", "Description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void add_ShouldNotContainDuplicates() {
        Task task = new Task("Test", "Description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        task.setId(1);

        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
    }

    @Test
    void remove_ShouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task1", "Desc1", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        Task task2 = new Task("Task2", "Desc2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(20));
        task1.setId(1);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void getHistory_ShouldReturnEmptyListForEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void remove_ShouldHandleNonExistentId() {
        Task task = new Task("Test", "Description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        task.setId(1);

        historyManager.add(task);
        historyManager.remove(999); // Несуществующий ID

        assertEquals(1, historyManager.getHistory().size());
    }
}