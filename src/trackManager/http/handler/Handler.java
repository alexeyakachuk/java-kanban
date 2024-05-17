package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import trackManager.controllers.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class Handler {
    protected TaskManager manager;
    public Handler(TaskManager manager) {
        this.manager = manager;
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    public void handleNoSuchElementException(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

    public void handleIOException(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, 0);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.sendResponseHeaders(406, 0);
        h.getResponseBody().write("Есть пересечения с существующими задачами".getBytes(StandardCharsets.UTF_8));
        h.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}




