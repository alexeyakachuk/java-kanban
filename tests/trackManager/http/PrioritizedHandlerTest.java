package trackManager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackManager.controllers.TaskManager;
import trackManager.model.Task;
import trackManager.utils.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrioritizedHandlerTest {
    private HttpTaskServer server;

    private final Gson gson = Managers.getGson();

    private final TaskManager manager = Managers.getDefault();
    private HttpClient client;

    @BeforeEach
    void init() throws IOException {

        server = new HttpTaskServer(manager);
        client = HttpClient.newHttpClient();
        server.start();
    }

    @Test
    void getPrioritizedTasksTest() throws IOException, InterruptedException {

        Task task1 = new Task("task1", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        Task task2 = new Task("task2", "описание", LocalDateTime.now().plusHours(1), Duration.ofMinutes(20));
        manager.createNewTask(task1);
        manager.createNewTask(task2);

        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritizedTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());

        assertNotNull(prioritizedTasks);
        assertEquals(200, response.statusCode());
        assertEquals(2, prioritizedTasks.size());
        assertEquals(task1.getNameTask(), prioritizedTasks.get(0).getNameTask());
        assertEquals(task2.getNameTask(), prioritizedTasks.get(1).getNameTask());
    }
}
