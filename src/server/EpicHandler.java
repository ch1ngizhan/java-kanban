package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;


    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        try {
            switch (method) {

                case "GET":
                    if (path.equals("/epics")) {
                        handleGetEpics(exchange);
                    } else if (path.startsWith("/epics/")) {
                        handleGetEpic(exchange);
                    } else if (path.endsWith("/subtasks")) {
                        handleGetEpicSubtasks(exchange);
                    }
                    break;

                case "POST":
                    if (path.equals("/epics")) {
                        handleCreateEpic(exchange);

                    } else if (path.startsWith("/epics/")) {
                        handleUpdateEpic(exchange);
                    }
                    break;
                case "DELETE":
                    handleDeleteEpic(exchange);
                    break;
                default:
                    sendText(exchange, "Указан не корректный метод!", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }


    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        Epic epic = taskManager.getByIDEpics(id);
        if (epic == null) {
            sendNotFound(exchange);
        }
        String response = gson.toJson(epic);
        sendText(exchange, response);
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getListEpics();
        String response = gson.toJson(epics);
        sendText(exchange, response);
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        if (taskManager.getByIDEpics(id) == null) {
            sendNotFound(exchange);
        }
        List<Subtask> subtasks = taskManager.getSubtaskForEpic(id);
        String response = gson.toJson(subtasks);
        sendText(exchange, response);
    }

    private void handleCreateEpic(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        taskManager.createEpic(epic);
        sendText(exchange, "Задача создана. ID:" + epic.getId(), 201);
    }

    private void handleUpdateEpic(HttpExchange exchange) throws IOException {

        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        taskManager.updateEpic(id, epic.getTitle(), epic.getDescription());
        sendText(exchange, "Задача обновлена.");

    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        taskManager.deleteEpic(id);
        sendText(exchange, "Задача удалена.");
    }
}
