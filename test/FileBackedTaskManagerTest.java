import manager.FileBackedTaskManager;
import model.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void saveAndLoad_ShouldPreserveTasks() throws IOException {
        Task task = new Task("Task", "Desc", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofMinutes(30));
        manager.createTask(task);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        Task loadedTask = loaded.getByIDTask(task.getId());

        assertNotNull(loadedTask);
        assertEquals(task.getTitle(), loadedTask.getTitle());
        assertEquals(task.getStartTime(), loadedTask.getStartTime());
    }

    @Test
    void loadFromFile_ShouldHandleEmptyFile() throws IOException {
        Files.write(tempFile.toPath(), "".getBytes());

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loaded.getListTask().isEmpty());
        assertTrue(loaded.getListEpics().isEmpty());
    }

    @Test
    void save_ShouldPreserveEpicSubtasksRelationship() throws IOException {
        Epic epic = new Epic("Epic", "Desc");
        manager.createEpic(epic);

        Subtask sub = new Subtask("Sub", "Desc", Status.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createSubtask(sub);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        List<Subtask> subs = loaded.getSubtaskForEpic(epic.getId());

        assertEquals(1, subs.size());
        assertEquals(sub.getTitle(), subs.get(0).getTitle());
    }
}