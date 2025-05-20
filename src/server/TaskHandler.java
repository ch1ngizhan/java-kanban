package server;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;



   public TaskHandler(TaskManager taskManager){
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
                   sendText(exchange, "Указан не коректный метод!", 405);
           }
       } catch (Exception e){
           sendText(exchange,"Internal Server Error",500);
       }


    }


    private void handleGetTask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        Task task = taskManager.getByIDTask(id);
        if(task == null){
           sendNotFound(exchange);
        }
        String response = gson.toJson(task);
        sendText(exchange,response);
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException{
        List<Task> tasks= taskManager.getListTask();
        String response = gson.toJson(tasks);
        sendText(exchange,response);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body,Task.class);
        if(taskManager.hasTimeIntersections(task)) {
            sendHasInteractions(exchange);
        }
        taskManager.createTask(task);
        sendText(exchange,"Задача создана.",201);
    }
    private void handleUpdateTask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body,Task.class);
        if(taskManager.hasTimeIntersections(task)) {
            sendHasInteractions(exchange);
        }
        taskManager.updateTask(id,task);
        sendText(exchange,"Задача обновлена.");
    }
    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        int id = (int)Long.parseLong(parts[2]);
        taskManager.deleteTask(id);
        sendText(exchange,"Задача удалина.");
    }
}






