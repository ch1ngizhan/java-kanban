package handlerTest;

import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskManagerEpicsTest extends HttpTaskManagerTestBase {

    @Test
    void testCreateEpic() throws Exception {
        Epic epic = new Epic("Test epic", "Description");
        String epicJson = gson.toJson(epic);

        HttpResponse<String> response = sendPostRequest("/epics", epicJson);

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getListEpics().size());
        Epic createdEpic = manager.getListEpics().get(0);
        assertEquals("Test epic", createdEpic.getTitle());
        assertEquals(Status.NEW, createdEpic.getStatus());
    }

    @Test
    void testUpdateTask() throws Exception {
        // Создаем задачу
        Epic epic = new Epic("Original", "Desc");
        manager.createEpic(epic);
        int epicId = epic.getId();

        // Подготавливаем обновленные данные
        Epic updatedEpic = new Epic("Updated", "New desc");
        updatedEpic.setId(epicId);
        String updatedJson = gson.toJson(updatedEpic);

        // Отправляем запрос на обновление
        HttpResponse<String> response = sendPostRequest("/epics/" + epicId, updatedJson);

        // Проверки
        assertEquals(200, response.statusCode(), "Неверный статус код");
        Epic taskFromManager = manager.getByIDEpics(epicId);
        assertEquals("Updated", taskFromManager.getTitle(), "Название не обновилось");
    }

    @Test
    void testGetEpicSubtasks() throws Exception {
        // Создаем эпик и подзадачи
        Epic epic = new Epic("Epic with subtasks", "");
        manager.createEpic(epic);
        int epicId = epic.getId();

        // Запрашиваем подзадачи эпика
        HttpResponse<String> response = sendGetRequest("/epics/" + epicId + "/subtasks");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("[]"), "Список подзадач должен быть пустым");
    }
}