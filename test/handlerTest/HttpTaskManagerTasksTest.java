package handlerTest;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HttpTaskManagerTasksTest extends HttpTaskManagerTestBase {

    @Test
    void testCreateTask() throws Exception {
        // Подготовка тестовых данных
        Task task = new Task("Test task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        // Отправка запроса
        HttpResponse<String> response = sendPostRequest("/tasks", taskJson);

        // Проверки
        assertEquals(201, response.statusCode(), "Неверный статус код");
        assertEquals(1, manager.getListTask().size(), "Задача не добавилась");
        Task createdTask = manager.getListTask().get(0);
        assertEquals("Test task", createdTask.getTitle(), "Название задачи не совпадает");
    }

    @Test
    void testGetTaskById() throws Exception {
        // Создаем задачу напрямую в менеджере
        Task task = new Task("Test task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createTask(task);
        int taskId = task.getId();

        // Запрашиваем задачу через API
        HttpResponse<String> response = sendGetRequest("/tasks/" + taskId);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(taskId, receivedTask.getId(), "ID задачи не совпадает");
        assertEquals("Test task", receivedTask.getTitle(), "Название задачи не совпадает");
    }

    @Test
    void testGetNonExistentTask() throws Exception {
        HttpResponse<String> response = sendGetRequest("/tasks/999");
        assertEquals(404, response.statusCode(), "Неверный статус код для несуществующей задачи");
    }

    @Test
    void testUpdateTask() throws Exception {
        // Создаем задачу
        Task task = new Task("Original", "Desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createTask(task);
        int taskId = task.getId();

        // Подготавливаем обновленные данные
        Task updatedTask = new Task("Updated", "New desc", Status.IN_PROGRESS,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(45));
        updatedTask.setId(taskId);
        String updatedJson = gson.toJson(updatedTask);

        // Отправляем запрос на обновление
        HttpResponse<String> response = sendPostRequest("/tasks/" + taskId, updatedJson);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        Task taskFromManager = manager.getByIDTask(taskId);
        assertEquals("Updated", taskFromManager.getTitle(), "Название не обновилось");
        assertEquals(Status.IN_PROGRESS, taskFromManager.getStatus(), "Статус не обновился");
    }

    @Test
    void testDeleteTask() throws Exception {
        // Создаем задачу
        Task task = new Task("To delete", "Desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createTask(task);
        int taskId = task.getId();

        // Удаляем задачу
        HttpResponse<String> response = sendDeleteRequest("/tasks/" + taskId);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        assertNull(manager.getByIDTask(taskId), "Задача не удалилась");
    }
}