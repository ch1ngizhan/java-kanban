package handlerTest;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HttpTaskManagerSubtasksTest extends HttpTaskManagerTestBase {


    @Test
    void testCreateSubtask() throws Exception {
        // Подготовка тестовых данных
        Epic epic = new Epic("Test epic", "Description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test task", "Description", Status.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(subtask);

        // Отправка запроса
        HttpResponse<String> response = sendPostRequest("/subtasks", taskJson);

        // Проверки
        assertEquals(201, response.statusCode(), "Неверный статус код");
        assertEquals(1, manager.getListSubtasks().size(), "Задача не добавилась");
        Subtask createdTask = manager.getListSubtasks().get(0);
        assertEquals("Test task", createdTask.getTitle(), "Название задачи не совпадает");
    }

    @Test
    void testGetSubtaskById() throws Exception {
        // Создаем задачу напрямую в менеджере
        Epic epic = new Epic("Test epic", "Description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test task", "Description", Status.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createSubtask(subtask);
        int subtaskId = subtask.getId();

        // Запрашиваем задачу через API
        HttpResponse<String> response = sendGetRequest("/subtasks/" + subtaskId);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        Subtask receivedTask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtaskId, receivedTask.getId(), "ID задачи не совпадает");
        assertEquals("Test task", receivedTask.getTitle(), "Название задачи не совпадает");
    }

    @Test
    void testGetNonExistentSubtask() throws Exception {
        HttpResponse<String> response = sendGetRequest("/subtasks/999");
        assertEquals(404, response.statusCode(), "Неверный статус код для несуществующей задачи");
    }

    @Test
    void testUpdateSubtask() throws Exception {
        // Создаем задачу
        Epic epic = new Epic("Test epic", "Description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test task", "Description", Status.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createSubtask(subtask);
        int subtaskId = subtask.getId();

        // Подготавливаем обновленные данные
        Subtask updatedSubtask = new Subtask("Updated", "Description", Status.IN_PROGRESS, epic.getId(),
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(45));
        updatedSubtask.setId(subtaskId);
        String updatedJson = gson.toJson(updatedSubtask);

        // Отправляем запрос на обновление
        HttpResponse<String> response = sendPostRequest("/subtasks/" + subtaskId, updatedJson);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        Subtask subtaskFromManager = manager.getByIDSubtasks(subtaskId);
        assertEquals("Updated", subtaskFromManager.getTitle(), "Название не обновилось");
        assertEquals(Status.IN_PROGRESS, subtaskFromManager.getStatus(), "Статус не обновился");
    }

    @Test
    void testDeleteSubtask() throws Exception {
        // Создаем задачу
        Epic epic = new Epic("Test epic", "Description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test task", "Description", Status.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createSubtask(subtask);
        int subtaskId = subtask.getId();

        // Удаляем задачу
        HttpResponse<String> response = sendDeleteRequest("/subtasks/" + subtaskId);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        assertNull(manager.getByIDTask(subtaskId), "Задача не удалилась");
    }

}