
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TaskTest {


    @Test
    public void addNewTask() throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getByIDTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void testTaskEqualityById() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.DONE);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    public void testEpicEqualityById() {
        Epic task1 = new Epic("Test addNewTask1", "Test addNewTask1 description");
        Epic task2 = new Epic("Test addNewTask2", "Test addNewTask2 description");
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    public void testSubtaskEqualityById() {
        Subtask task1 = new Subtask("Test addNewTask", "Test addNewTask description",
                Status.NEW, 1);
        Subtask task2 = new Subtask("Test addNewTask", "Test addNewTask description",
                Status.NEW, 2);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    public void testManagersReturnInitializedInstances() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }

    @Test
    public void testTaskManagerAddAndFindTasks() throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("Test addNewTask1", "Test addNewTask1 description");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description",
                Status.NEW, 2);
        taskManager.createSubtask(subtask);

        assertEquals(task, taskManager.getByIDTask(task.getId()));
        assertEquals(epic, taskManager.getByIDEpics(epic.getId()));
        assertEquals(subtask, taskManager.getByIDSubtasks(subtask.getId()));


    }

    @Test
    public void testTaskIdConflict() throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("model.Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("model.Task 2", "Description 2", Status.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        task1.setId(3);

        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void testTaskImmutabilityWhenAdded() throws IOException {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("model.Task 1", "Description 1", Status.NEW);
        taskManager.createTask(task1);

        Task task2 = taskManager.getByIDTask(task1.getId());
        assertEquals(task1.getTitle(), task2.getTitle());
        assertEquals(task1.getDescription(), task2.getDescription());
        assertEquals(task1.getStatus(), task2.getStatus());
    }

    @Test
    public void add() {
        Task task = new Task("model.Task 1", "Description 1", Status.NEW);
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}


