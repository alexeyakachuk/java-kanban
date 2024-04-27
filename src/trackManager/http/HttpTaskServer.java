package trackManager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import trackManager.controllers.InMemoryTaskManager;
import trackManager.controllers.TaskManager;
import trackManager.http.handler.EpicHandler;
import trackManager.http.handler.SubTaskHandler;
import trackManager.http.handler.TaskHandler;
import trackManager.utils.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;

    private HttpServer server;
    protected Gson gson;
    protected TaskManager taskManager;





    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.start();
        httpServer.createContext("/tasks", new TaskHandler(Managers.getDefault()));
        httpServer.createContext("/epics", new EpicHandler(Managers.getDefault()));

    }


}
