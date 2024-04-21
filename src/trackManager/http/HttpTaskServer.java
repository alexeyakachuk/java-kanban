package trackManager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
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


    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT),0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/subtasks", new SubTaskHandler());
    }

    public void start() {
        System.out.println("Started TaskServer " + PORT);
        System.out.println("http://localhost:" + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h, String text) throws IOException {
        return new String((h.getRequestBody().readAllBytes()), UTF_8);
    }

    public void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
    public Gson getGson() {
        return gson;
    }
    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();
    }


}
