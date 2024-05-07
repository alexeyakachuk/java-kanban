package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import trackManager.controllers.TaskManager;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PrioritizedHandler extends Handler implements HttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!requestMethod.equals("GET")) {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
            return;
        }
        try {
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            String jsonResponse = Managers.getGson().toJson(prioritizedTasks);
            exchange.getResponseHeaders()
                    .set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(jsonResponse.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }
}
