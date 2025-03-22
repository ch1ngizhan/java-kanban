import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {


        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что задачи отсутствуют
        assertTrue(loadedManager.getListTask().isEmpty());
        assertTrue(loadedManager.getListEpics().isEmpty());
        assertTrue(loadedManager.getListSubtasks().isEmpty());
    }

    @Test
    void testSaveAndLoadTasks() throws IOException {
        // Создаем задачи
        Task task1 = new Task("Task 1", "Description 1",Status.NEW);
        manager.createTask(task1);
        Epic epic1 = new Epic("Epic 1", "Description Epic 1");
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description Subtask 1",Status.NEW, epic1.getId());
        manager.createSubtask(subtask1);


        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что задачи загружены корректно
        assertEquals(1, loadedManager.getListTask().size());
        assertEquals(1, loadedManager.getListEpics().size());
        assertEquals(1, loadedManager.getListSubtasks().size());

        Task loadedTask = loadedManager.getByIDTask(task1.getId());
        Epic loadedEpic = loadedManager.getByIDEpics(epic1.getId());
        Subtask loadedSubtask = loadedManager.getByIDSubtasks(subtask1.getId());

        assertNotNull(loadedTask);
        assertNotNull(loadedEpic);
        assertNotNull(loadedSubtask);

        assertEquals(task1.getTitle(), loadedTask.getTitle());
        assertEquals(epic1.getTitle(), loadedEpic.getTitle());
        assertEquals(subtask1.getTitle(), loadedSubtask.getTitle());
    }

    @Test
    void testSaveAndLoadAfterUpdate() throws IOException {
        // Создаем задачу
        Task task1 = new Task("Task 1", "Description 1",Status.NEW);
        manager.createTask(task1);

        // Обновляем задачу
        task1.setStatus(Status.DONE);
        manager.updateTask(task1.getId(),task1);

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что статус задачи обновлен
        Task loadedTask = loadedManager.getByIDTask(task1.getId());
        assertNotNull(loadedTask);
        assertEquals(Status.DONE, loadedTask.getStatus());
    }

    @Test
    void testSaveAndLoadAfterDelete() throws IOException {
        // Создаем задачу
        Task task1 = new Task("Task 1", "Description 1",Status.NEW);
        manager.createTask(task1);

        // Удаляем задачу
        manager.deleteTask(task1.getId());



        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что задача удалена
        assertTrue(loadedManager.getListTask().isEmpty());
    }


}