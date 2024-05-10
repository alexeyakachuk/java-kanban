package trackManager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.Status;
import trackManager.model.SubTask;

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

public class EpicHandlerTest {
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
    void createEpicTest() throws IOException, InterruptedException {

        Epic epic = new Epic("epic", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        String jsonNewTask = gson.toJson(epic);
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonNewTask))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание", LocalDateTime.now(), Duration.ofMinutes(10));
        manager.createNewEpic(epic);
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals(1, epics.size());
    }

    @Test
    void getEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        int id = manager.createNewEpic(epic);
        URI uri = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(), Epic.class);

        assertNotNull(epic1);
        assertEquals(200, response.statusCode());
        assertEquals(epic.getNameTask(), epic1.getNameTask());
        assertEquals(epic.getDescriptionTask(), epic1.getDescriptionTask());
    }

    @Test
    void getSubTaskOfEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask, epic);
        URI uri = URI.create("http://localhost:8080/epics/" + epicId + "/subTasks");
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
    void epicStatusTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        SubTask subTask = new SubTask("subTask", "описание",
                LocalDateTime.now(), Duration.ofMinutes(10));
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask, epic);
        subTask.setStatusTask(Status.DONE);
        manager.updateSubTask(subTask);
        String jsonNewEpic = gson.toJson(epic);
        URI uri = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(), Epic.class);

        assertNotNull(epic1);
        assertEquals(200, response.statusCode());
        assertEquals(Status.DONE, epic1.getStatusTask());
    }

    @Test
    void updateEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        int id = manager.createNewEpic(epic);
        epic.setNameTask("epic1");
        epic.setDescriptionTask("описание1");
        manager.updateEpic(epic);
        String jsonNewEpic = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + id))
                .POST(HttpRequest.BodyPublishers.ofString(jsonNewEpic))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = manager.getEpicById(id);

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals("epic1", epic1.getNameTask());
        assertEquals("описание1", epic1.getDescriptionTask());
    }

    @Test
    void deleteEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "описание");
        int id = manager.createNewEpic(epic);
        URI uri = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
    }
}
