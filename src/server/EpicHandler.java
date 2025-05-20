package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler  {

    private final TaskManager taskManager;
    private final Gson gson;



    public EpicHandler(TaskManager taskManager){
        this.taskManager = taskManager;
        gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
           try{
            switch (method){

                case "GET":
                    if(path.equals("/epics")){
                        handleGetEpics(exchange);
                    } else if (path.startsWith("/epics/")) {
                        handleGetEpic(exchange);
                    } else if (path.endsWith("/subtasks")) {
                        handleGetEpicSubtasks(exchange);
                    }
                    break;

                case "POST":
                    handleCreateEpic(exchange);
                    break;

                case "DELETE":
                    handleDeleteEpic(exchange);
                    break;
                default:
                    sendText(exchange, "Указан не коректный метод!", 405);
            }
    } catch (Exception e){
        sendText(exchange,"Internal Server Error",500);
    }


}

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        Epic epic = taskManager.getByIDEpics(id);
        if(epic == null){
            sendNotFound(exchange);
        }
        String response = gson.toJson(epic);
        sendText(exchange,response);
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics= taskManager.getListEpics();
        String response = gson.toJson(epics);
        sendText(exchange,response);
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        if(taskManager.getByIDEpics(id) == null){
            sendNotFound(exchange);
        }
        List<Subtask> subtasks = taskManager.getSubtaskForEpic(id);
        String response = gson.toJson(subtasks);
        sendText(exchange,response);
    }

    private void handleCreateEpic(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body,Epic.class);

        taskManager.createEpic(epic);
        sendText(exchange,"Задача создана.",201);
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        taskManager.deleteEpic(id);
        sendText(exchange,"Задача удалина.");
    }
}
