package trackManager.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackManager.controllers.TaskManager;
import trackManager.model.Epic;
import trackManager.model.SubTask;
import trackManager.utils.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}



