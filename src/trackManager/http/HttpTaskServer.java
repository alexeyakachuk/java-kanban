package trackManager.http;

import com.sun.net.httpserver.HttpServer;
import trackManager.controllers.TaskManager;
import trackManager.http.handler.*;
import trackManager.utils.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;



    public HttpTaskServer(TaskManager manager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/subTasks", new SubTaskHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }
    public void start() {
        System.out.println("Старт сервера на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("Остановка сервера на порту " + PORT);
        httpServer.stop(0);
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
        //server.stop();

//        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
//        TaskManager manager = Managers.getDefault();
//
//        httpServer.createContext("/tasks", new TaskHandler(manager));
//        httpServer.createContext("/epics", new EpicHandler(manager));
//        httpServer.createContext("/subTasks", new SubTaskHandler(manager));
//        httpServer.createContext("/history", new HistoryHandler(manager));
//        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
//
//
//        httpServer.start();
//        httpServer.stop(0);
    }
}