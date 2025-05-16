import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    // Тесты для задач
    @Test
    void createTask_ShouldAddTaskAndGenerateId() {
        Task task = new Task("Test", "Description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.createTask(task);

        Task savedTask = taskManager.getByIDTask(task.getId());
        assertNotNull(savedTask);
        assertEquals(task.getTitle(), savedTask.getTitle());
    }

    @Test
    void updateTask_ShouldUpdateExistingTask() {
        Task task = new Task("Original", "Desc", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.createTask(task);

        Task updated = new Task("Updated", "New Desc", Status.IN_PROGRESS,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));
        updated.setId(task.getId());

        taskManager.updateTask(task.getId(), updated);
        Task saved = taskManager.getByIDTask(task.getId());

        assertEquals("Updated", saved.getTitle());
        assertEquals(Status.IN_PROGRESS, saved.getStatus());
    }

    // Тесты для эпиков
    @Test
    void createEpic_ShouldCalculateStatusAutomatically() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatus_ShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic", "Desc");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", Status.DONE, epic.getId(), null, null);
        Subtask sub2 = new Subtask("Sub2", "Desc", Status.DONE, epic.getId(), null, null);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    // Тесты для временных меток
    @Test
    void getPrioritizedTasks_ShouldReturnTasksInTimeOrder() {
        Task early = new Task("Early", "Desc", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 9, 0), Duration.ofMinutes(30));
        Task late = new Task("Late", "Desc", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofMinutes(30));

        taskManager.createTask(late);
        taskManager.createTask(early);

        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(early, prioritized.get(0));
        assertEquals(late, prioritized.get(1));
    }

    @Test
    void createTask_ShouldThrowWhenTimeOverlaps() {
        Task task1 = new Task("Task1", "Desc", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofMinutes(60));
        taskManager.createTask(task1);

        Task task2 = new Task("Task2", "Desc", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 30), Duration.ofMinutes(30));

        assertThrows(ManagerSaveException.class, () -> taskManager.createTask(task2));
    }
}