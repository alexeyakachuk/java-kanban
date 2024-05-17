package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import trackManager.controllers.TaskManager;
import trackManager.exception.NotFoundException;
import trackManager.exception.UrlException;
import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.utils.Managers;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends Handler implements HttpHandler {
    public EpicHandler(TaskManager manager) {
        super(manager);
    }
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if (!isValidPath(path)) {
                System.out.println("Неправильный запрос: " + path);
                exchange.sendResponseHeaders(404, 0); // Отправляем код ошибки 404
                return;
            }

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        getEpic(exchange);
                    }

                    if (Pattern.matches("^/epics$", path)) {
                        getEpics(exchange);
                    }

                    if (Pattern.matches("^/epics/\\d+/subTasks$", path)){
                        getSubTaskOfEpic(exchange);}
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        createEpic(exchange);
                    }

                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        updateEpic(exchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        deleteEpic(exchange);
                    }
                    break;
                }
                default: {
                    System.out.println("Неправельный запрос");
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (IOException e) {
            throw new UrlException(e.getMessage());
        } finally {
            exchange.close();
        }
    }

    private boolean isValidPath(String path) {
        return Pattern.matches("^/epics(/\\d+)?(/subTasks)?$", path);
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = manager.getAllEpics();
        String jsonResponse = Managers.getGson().toJson(epics);

        sendText(exchange, jsonResponse);
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        try {
            String[] split = exchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(split[2]);
            Epic epic = manager.getEpicById(id);

            String jsonResponse = Managers.getGson().toJson(epic);

            sendText(exchange, jsonResponse);

        } catch (NotFoundException e) {
            handleNoSuchElementException(exchange);
        } catch (IOException e) {
            handleIOException(exchange);
        }
    }

    private void getSubTaskOfEpic(HttpExchange exchange) throws IOException {
        try {
            String[] split = exchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(split[2]);
            Epic epic = manager.getEpicById(id);
            List<SubTask> epicAllSubtask = manager.getEpicAllSubtask(epic);

            String jsonResponse = Managers.getGson().toJson(epicAllSubtask);
            sendText(exchange, jsonResponse);

        } catch (NotFoundException e) {
            handleNoSuchElementException(exchange);
        } catch (IOException e) {
            handleIOException(exchange);
        }
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Epic epic = Managers.getGson()
                .fromJson(body, Epic.class);

        try {
            manager.createNewEpic(epic);
            exchange.sendResponseHeaders(201, 0);
        } catch (Exception e) {
            exchange.sendResponseHeaders(406, 0);
        }
    }

    private void updateEpic(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Epic epic = Managers.getGson().fromJson(body,Epic.class);
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        epic.setId(id);

        try {
            manager.updateEpic(epic);
            exchange.sendResponseHeaders(200,0);
        }catch (Exception e) {
            exchange.sendResponseHeaders(406,0);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        manager.deleteByIdEpic(id);
        exchange.sendResponseHeaders(204, 0);
    }
}