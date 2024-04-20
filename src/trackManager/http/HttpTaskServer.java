package trackManager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import trackManager.controllers.TaskManager;
import trackManager.utils.Managers;

public class HttpTaskServer {

    public static final int PORT = 8080;

    private HttpServer server;
    private Gson gson;
    protected TaskManager taskManager;


    public HttpTaskServer() {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


}
