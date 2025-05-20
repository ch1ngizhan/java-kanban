package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler{

    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager){
        this.taskManager = taskManager;
        gson = new Gson();
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
           try{
               if (method.equals("GET")){
                   List<Task> history = taskManager.getHistory();
                   String response = gson.toJson(history);
                   sendText(exchange,response);
               }else{
                   sendText(exchange, "Указан не коректный метод!", 405);
               }
           }catch (Exception e){
               sendText(exchange,"Internal Server Error",500);
           }
    }
}
