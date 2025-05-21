package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;


    public SubtaskHandler(TaskManager taskManager) {
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
                    if (path.equals("/subtasks")) {
                        handleGetSubtasks(exchange);

                    } else if (path.startsWith("/subtasks/")) {
                        handleGetSubtask(exchange);
                    }
                    break;

                case "POST":
                    if (path.equals("/subtasks")) {
                        handleCreateSubtask(exchange);

                    } else if (path.startsWith("/subtasks/")) {
                        handleUpdateSubtask(exchange);
                    }
                    break;

                case "DELETE":
                    if (path.equals("/subtasks")) {
                        sendText(exchange, "Не указан id! ", 401);
                    } else if (path.startsWith("/subtasks/")) {
                        handleDeleteSubtask(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Указан не корректный метод!", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }


    }


    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        Subtask subtask = taskManager.getByIDSubtasks(id);
        if (subtask == null) {
            sendNotFound(exchange);
            return;
        }
        String response = gson.toJson(subtask);
        sendText(exchange, response);
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getListSubtasks();
        String response = gson.toJson(subtasks);
        sendText(exchange, response);
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        if (taskManager.hasTimeIntersections(subtask)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.createSubtask(subtask);
        sendText(exchange, "Задача создана. ID:" + subtask.getId(), 201);
    }

    private void handleUpdateSubtask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        if (taskManager.hasTimeIntersections(subtask)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.updateSubtask(id, subtask);
        sendText(exchange, "Задача обновлена.");
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        taskManager.deleteSubtask(id);
        sendText(exchange, "Задача удалена.");
    }
}
