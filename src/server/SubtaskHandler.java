package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import manager.TaskManager;
import model.Subtask;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler  {

    private final TaskManager taskManager;
    private final Gson gson;



    public SubtaskHandler(TaskManager taskManager){
        this.taskManager = taskManager;
        gson = new Gson();
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        try {
            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {
                        handleGetSubtasks(exchange);

                    } else if (path.startsWith("/tasks/")) {
                        handleGetSubtask(exchange);
                    }
                    break;

                case "POST":
                    if (path.equals("/tasks")) {
                        handleCreateSubtask(exchange);

                    } else if (path.startsWith("/tasks/")) {
                        handleUpdateSubtask(exchange);
                    }
                    break;

                case "DELETE":
                    if (path.equals("/tasks")) {
                        sendText(exchange, "Не указан id! ", 401);
                    } else if (path.startsWith("/tasks/")) {
                        handleDeleteSubtask(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Указан не коректный метод!", 405);
            }
        } catch (Exception e){
            sendText(exchange,"Internal Server Error",500);
        }


    }


    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        Subtask subtask = taskManager.getByIDSubtasks(id);
        if(subtask == null){
            sendNotFound(exchange);
        }
        String response = gson.toJson(subtask);
        sendText(exchange,response);
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException{
        List<Subtask> subtasks= taskManager.getListSubtasks();
        String response = gson.toJson(subtasks);
        sendText(exchange,response);
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body,Subtask.class);
        if(taskManager.hasTimeIntersections(subtask)) {
            sendHasInteractions(exchange);
        }
        taskManager.createSubtask(subtask);
        sendText(exchange,"Задача создана.",201);
    }
    private void handleUpdateSubtask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body,Subtask.class);
        if(taskManager.hasTimeIntersections(subtask)) {
            sendHasInteractions(exchange);
        }
        taskManager.updateSubtask(id,subtask);
        sendText(exchange,"Задача обновлена.");
    }
    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        taskManager.deleteSubtask(id);
        sendText(exchange,"Задача удалина.");
    }
}
