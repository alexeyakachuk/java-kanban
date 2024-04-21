package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import trackManager.controllers.InMemoryTaskManager;
import trackManager.http.HttpTaskServer;

import java.util.regex.Pattern;


public class TaskHandler implements HttpHandler {
    HttpTaskServer taskServer;
    InMemoryTaskManager inMemoryTaskManager;


    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if(Pattern.matches("^/tasks$", path)) {
                        String response = taskServer.getGson().toJson(inMemoryTaskManager.getAllTasks());
                        taskServer.sendText(httpExchange, response);
                        return;

                    }

                    break;
                }
                case "POST": {

                    break;
                }
                case "DELETE": {

                    break;
                }
                default: {
                    System.out.println("Неправельный запрос");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

}
