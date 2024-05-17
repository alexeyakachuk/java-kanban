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

public class SubTaskHandler extends Handler implements HttpHandler {
    public SubTaskHandler(TaskManager manager) {
        super(manager);
    }
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();

            if (!isValidPath(path)) {
                System.out.println("Неправильный запрос: " + path);
                exchange.sendResponseHeaders(404, 0); // Отправляем код ошибки 404
                return;
            }

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/subTasks$", path)) {
                        getSubTasks(exchange);
                    }

                    if (Pattern.matches("^/subTasks/\\d+$", path)) {
                        getSubtask(exchange);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/subTasks$", path)) {
                        createSubTask(exchange);
                    }

                    if (Pattern.matches("^/subTasks/\\d+$", path)) {
                        updateSubTask(exchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/subTasks/\\d+$", path)) {
                        deleteSubTask(exchange);
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
        return Pattern.matches("^/subTasks(/\\d+)?$", path);
    }

    private void getSubTasks(HttpExchange exchange) throws IOException {
        List<SubTask> allSubTasks = manager.getAllSubTasks();
        String jsonResponse = Managers.getGson().toJson(allSubTasks);

        sendText(exchange, jsonResponse);
    }

    private void getSubtask(HttpExchange exchange) throws IOException {
        try {
            String[] split = exchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(split[2]);
            SubTask subTask = manager.getSubTaskById(id);

            String jsonResponse = Managers.getGson().toJson(subTask);

            sendText(exchange, jsonResponse);

        } catch (NotFoundException e) {
            handleNoSuchElementException(exchange);
        } catch (IOException e) {
            handleIOException(exchange);
        }
    }

    private void createSubTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        SubTask subTask = Managers.getGson()
                .fromJson(body, SubTask.class);

        try {
            Integer id = subTask.getEpicId();
            Epic epic = manager.getEpicById(id);
            manager.createNewSubTask(subTask, epic);
            exchange.sendResponseHeaders(201, 0);
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void updateSubTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        SubTask subTask = Managers.getGson()
                .fromJson(body, SubTask.class);
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        subTask.setId(id);

        try {
            manager.updateSubTask(subTask);
            exchange.sendResponseHeaders(200,0);
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void deleteSubTask(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        manager.deleteByIdSubTask(id);
        exchange.sendResponseHeaders(204,0);
    }
}





