package trackManager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.SubTask;
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

public class SubTaskHandlerTest {
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
    void createSubTaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        subTask.setEpicId(epicId);
        String jsonRequest = gson.toJson(subTask);
        URI uri = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void getSubtasksTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask, epic);
        URI uri = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals(1, subTasks.size());
    }

    @Test
    void getSubTaskTest() throws IOException, InterruptedException{
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask, epic);
        URI uri = URI.create("http://localhost:8080/subTasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = gson.fromJson(response.body(), SubTask.class);

        assertNotNull(subTask1);
        assertEquals(200, response.statusCode());
        assertEquals(subTask.getNameTask(), subTask1.getNameTask());
        assertEquals(subTask.getDescriptionTask(), subTask1.getDescriptionTask());
    }

    @Test
    void updateSubTaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask, epic);
        subTask.setNameTask("subTask1");
        subTask.setDescriptionTask("описание1");
        manager.updateSubTask(subTask);
        String jsonNewSubTask = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subTasks/" + subTaskId))
                .POST(HttpRequest.BodyPublishers.ofString(jsonNewSubTask))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = manager.getSubTaskById(subTaskId);

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals("subTask1", subTask1.getNameTask());
        assertEquals("описание1", subTask1.getDescriptionTask());
    }

    @Test
    void deleteSubTaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask, epic);
        URI uri = URI.create("http://localhost:8080/subTasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
    }
}



