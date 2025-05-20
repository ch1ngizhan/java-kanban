package server;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final TaskManager taskManager;

public HttpTaskServer(TaskManager taskManager){
    this.taskManager = taskManager;
}
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpServer httpServer = HttpServer.create();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start(httpServer);
    }


    public void start(HttpServer httpServer) throws IOException {

        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks",new TaskHandler(taskManager));
        httpServer.createContext("/subtasks",new SubtaskHandler(taskManager));
        httpServer.createContext("/epics",new EpicHandler(taskManager));
        httpServer.createContext("/history",new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized",new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public void stop(HttpServer httpServer){
        httpServer.stop(1);
    }
}




