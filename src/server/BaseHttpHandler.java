package server;



import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {




    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
//для отправки ответа в случае, если объект не был найден
    protected void sendNotFound(){}


//для отправки ответа, если при создании или обновлении задача пересекается с уже существующими
    protected void sendHasInteractions(){}

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
    protected void sendText(HttpExchange exchange, int statusCode, String text) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, text.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }
}
