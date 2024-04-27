package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler implements HttpHandler{
    protected TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;

    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/epics$", path)) {
                        getEpics(exchange);
                    }
                    if (Pattern.matches("^/epics$/\\d+$", path)) {
                        getEpic(exchange);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)){

                    }
                    break;
                }
                case "DELETE": {

                    break;
                }
                default: {
                    System.out.println("Неправельный запрос");
                    exchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }


    private void getEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = manager.getAllEpics();
        String jsonResponse = Managers.getGson().toJson(epics);

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.close();

    }

    private void getEpic(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        Epic epic = manager.getEpicById(id);
        String jsonResponse = Managers.getGson().toJson(epic);

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200,0);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = Managers.getGson()
                .fromJson(body, Epic.class);
    }



}