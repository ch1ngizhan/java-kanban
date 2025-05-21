package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;


    public TaskHandler(TaskManager taskManager) {
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
                    if (path.equals("/tasks")) {
                        handleGetTasks(exchange);

                    } else if (path.startsWith("/tasks/")) {
                        handleGetTask(exchange);
                    }
                    break;

                case "POST":
                    if (path.equals("/tasks")) {
                        handleCreateTask(exchange);

                    } else if (path.startsWith("/tasks/")) {
                        handleUpdateTask(exchange);
                    }
                    break;

                case "DELETE":
                    if (path.equals("/tasks")) {
                        sendText(exchange, "Не указан id! ", 401);
                    } else if (path.startsWith("/tasks/")) {
                        handleDeleteTask(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Указан не корректный метод!", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }


    }


    private void handleGetTask(HttpExchange exchange) throws IOException {
        try {
            String[] parts = exchange.getRequestURI().getPath().split("/");
            if (parts.length < 3) {
                sendText(exchange, "Некорректный URL", 400);
                return;
            }
            long id = Long.parseLong(parts[2]);
            Task task = taskManager.getByIDTask((int) id);
            if (task == null) {
                sendNotFound(exchange);
                return;
            }
            String response = gson.toJson(task);
            sendText(exchange, response, 200);
        } catch (NumberFormatException e) {
            sendText(exchange, "ID задачи должен быть числом", 400);
        } catch (Exception e) {
            sendText(exchange, "Внутренняя ошибка сервера", 500);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getListTask();
        String response = gson.toJson(tasks);
        sendText(exchange, response);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);
        if (taskManager.hasTimeIntersections(task)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.createTask(task);
        sendText(exchange, "Задача создана.", 201);
    }

    private void handleUpdateTask(HttpExchange exchange) throws IOException {

        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);
        if (taskManager.hasTimeIntersections(task)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.updateTask(id, task);
        sendText(exchange, "Задача обновлена.");

    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int) Long.parseLong(parts[2]);
        taskManager.deleteTask(id);
        sendText(exchange, "Задача удалина.");
    }
}






