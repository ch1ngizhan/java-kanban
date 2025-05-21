package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer server;
    private final TaskManager taskManager;
    protected HttpClient client;
    protected Gson gson;
    protected final String baseUrl = "http://localhost:8080";

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        registerHandlers();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
        taskManager.createTask(new Task("Original", "Desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30)));
        HttpResponse<String> response = server.request();
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }


    public void start() throws IOException {
        server.start();
        System.out.println("HTTP-сервер запущен на порту 8080");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    private void registerHandlers() {
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    private HttpResponse<String> request() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        Epic task = new Epic("Test task", "Description");
        String taskJson = gson.toJson(task);

        // Отправка запроса

        URI url = URI.create(baseUrl + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}




