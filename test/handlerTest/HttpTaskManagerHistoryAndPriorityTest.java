package handlerTest;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskManagerHistoryAndPriorityTest extends HttpTaskManagerTestBase {

    @Test
    void testGetHistory() throws Exception {
        // Создаем и запрашиваем несколько задач
        Task task1 = new Task("Task 1", "Desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10));
        manager.createTask(task1);
        manager.getByIDTask(task1.getId());

        HttpResponse<String> response = sendGetRequest("/history");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task 1"), "История должна содержать задачу");
    }

    @Test
    void testGetPrioritizedTasks() throws Exception {
        // Создаем задачи с разным временем
        Task task1 = new Task("Early", "Desc", Status.NEW,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));
        Task task2 = new Task("Late", "Desc", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(30));
        manager.createTask(task1);
        manager.createTask(task2);

        HttpResponse<String> response = sendGetRequest("/prioritized");

        assertEquals(200, response.statusCode());
        // Проверяем порядок задач (раньше должна быть первой)
        assertTrue(response.body().indexOf("Early") < response.body().indexOf("Late"));
    }
}