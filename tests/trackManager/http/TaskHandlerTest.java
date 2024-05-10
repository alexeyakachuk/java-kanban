package trackManager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {

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

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void createTaskTest() throws IOException, InterruptedException {

        Task task = new Task("task", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        String jsonNewTask = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonNewTask))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void getTasksTest() throws IOException, InterruptedException {
        Task task = new Task("task", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        manager.createNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals(1, tasks.size());
    }

    @Test
    void getTaskTest() throws IOException, InterruptedException {
        Task task = new Task("task", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        int id = manager.createNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);

        assertNotNull(task1);
        assertEquals(200, response.statusCode());
        assertEquals(task.getNameTask(), task1.getNameTask());
        assertEquals(task.getDescriptionTask(), task1.getDescriptionTask());
    }

    @Test
    void updateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("task", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        int id = manager.createNewTask(task);
        task.setNameTask("task1");
        task.setDescriptionTask("описание1");
        manager.updateTask(task);
        String jsonNewTask = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + id))
                .POST(HttpRequest.BodyPublishers.ofString(jsonNewTask))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task1 = manager.getTaskById(id);
        System.out.println(task1);
        assertNotNull(response);

        assertEquals(200, response.statusCode());
        assertEquals("task1", task1.getNameTask());
        assertEquals("описание1", task1.getDescriptionTask());
    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {
        Task task = new Task("task", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        int id = manager.createNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());

    }
}
