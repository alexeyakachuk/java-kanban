package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import trackManager.controllers.TaskManager;
import trackManager.exception.NotFoundException;
import trackManager.exception.UrlException;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;


public class TaskHandler extends Handler implements HttpHandler {
    public TaskHandler(TaskManager manager) {
        super(manager);
    }



    @Override
    public void handle(HttpExchange exchange)  {
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
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        getTask(exchange);
                    }

                    if (Pattern.matches("^/tasks$", path)) {
                        getTasks(exchange);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        updateTask(exchange);
                    }

                    if (Pattern.matches("^/tasks$", path)) {
                        createTask(exchange);
                    }
                    break;
                }
                case "DELETE": {

                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        deleteTask(exchange);
                    }
                    break;
                }
                default:
                    System.out.println("Не правильный запрос");
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new UrlException(e.getMessage());
        }
        finally {
            exchange.close();
        }
    }

    private boolean isValidPath(String path) {
        return Pattern.matches("^/tasks(/\\d+)?$", path);
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = manager.getAllTasks();
        String jsonResponse = Managers.getGson().toJson(tasks);

        sendText(exchange, jsonResponse);
    }

    private void getTask(HttpExchange exchange) throws IOException {
        try {
            String[] split = exchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(split[2]);
            Task task = manager.getTaskById(id);


            String jsonResponse = Managers.getGson().toJson(task);

            sendText(exchange, jsonResponse);
        } catch (NotFoundException e) {
            handleNoSuchElementException(exchange);
        } catch (IOException e) {
            handleIOException(exchange);
        }
    }

    private void createTask(HttpExchange exchange) throws IOException {

        String body = readText(exchange);

        Task task = Managers.getGson()
                .fromJson(body, Task.class);

        try {
            manager.createNewTask(task);
            exchange.sendResponseHeaders(201, 0);
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void updateTask(HttpExchange exchange) throws IOException {

        String body = readText(exchange);
        Task task = Managers.getGson().fromJson(body, Task.class);
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        task.setId(id);

        try {
            manager.updateTask(task);
            exchange.sendResponseHeaders(200, 0);
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        manager.deleteByIdTask(id);
        exchange.sendResponseHeaders(204, 0);
    }
}
