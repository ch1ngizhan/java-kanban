import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import manager.TaskManager;
import model.Task;
import server.BaseHttpHandler;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    private TaskManager taskManager;
    private JsonParser jsonParser;



   public TaskHandler(TaskManager taskManager){
       this.taskManager = taskManager;
       jsonParser = new JsonParser();
   }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
    private void handleGetTask(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        Long id = Long.parseLong(parts[2]);
        Task task = taskManager.getTask(id);
        if(task==null){

        }
        String response = jsonParser.toJson(task);
        sendResponse(exchange, 200, response);
    }
}






