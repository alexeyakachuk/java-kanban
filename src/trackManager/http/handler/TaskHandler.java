package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import trackManager.controllers.TaskManager;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;


public class TaskHandler implements HttpHandler {
    protected TaskManager manager;


    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        getTask(exchange);
                    }
                    if (Pattern.matches("^/tasks$", path)) {
                        getTasks(exchange);
                    }

//                    TODO возвращать 404

                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        updateTask(exchange);
                    }
                    if (Pattern.matches("^/tasks$", path)){
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


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }

    }

    public void getTasks(HttpExchange exchange) throws IOException {


        List<Task> tasks = manager.getAllTasks();
        String jsonResponse = Managers.getGson().toJson(tasks);

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }

    private void getTask(HttpExchange exchange) throws IOException {

        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        Task task = manager.getTaskById(id);

        String jsonResponse = Managers.getGson().toJson(task);


        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }

    private void createTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = Managers.getGson()
                .fromJson(body, Task.class);

        try {
            manager.createNewTask(task);
            exchange.sendResponseHeaders(201, 0);
        } catch (Exception e) {
            exchange.sendResponseHeaders(406,0);
        }

    }

    private void updateTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = Managers.getGson().fromJson(body, Task.class);
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        task.setId(id);

        try{
            manager.updateTask(task);
            exchange.sendResponseHeaders(200,0);
        } catch (Exception e) {
            exchange.sendResponseHeaders(406,0);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(split[2]);
        manager.deleteByIdTask(id);
        exchange.sendResponseHeaders(204,0);


    }
}
