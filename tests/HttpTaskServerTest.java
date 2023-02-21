import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.manager.HttpTaskManager;
import main.manager.TaskManager;
import main.server.HttpTaskServer;
import main.server.KVServer;
import main.service.LocalDateTimeTypeAdapter;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.SubTask;
import main.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer httpServer;
    HttpTaskManager manager;
    KVServer kvServer;
    HttpClient client;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).create();

    @BeforeEach
    public void init() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager("http://localhost:8078/");
        client = HttpClient.newHttpClient();
        httpServer = new HttpTaskServer(manager);
    }

    @AfterEach
    public void exit() {
        httpServer.stop();
        kvServer.stop();
    }

    @Test
    public void shouldReturnTaskToPOST() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task("одеться", "надеть рубашку", Status.IN_PROGRESS, 1,
                LocalDateTime.of(2023, Month.FEBRUARY, 4, 10, 10));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturnEpicToPOST() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic?id=1");
        Epic epic = new Epic("одеться", "надеть рубашку");
        manager.createEpic(epic);
        Epic epic1 = new Epic(1, "одеться", "надеть рубашку", 15
                , LocalDateTime.of(2023, Month.FEBRUARY, 8, 22, 1), Status.IN_PROGRESS);
        String jsonEpic = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturnTaskToGET() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task?id=1");
        Task task = new Task("одеться", "надеть рубашку");
        manager.createTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(task), response.body());
    }

    @Test
    public void shouldReturnSubtaskToGET() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask?id=2");
        Epic epic = new Epic("первый эпик", "описание первого эпика");
        manager.createEpic(epic);
        SubTask subtask = new SubTask("одеться", "надеть рубашку", epic.getId());
        manager.createSubTask(subtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(subtask), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturnEpicToGET() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic?id=1");
        Epic epic = new Epic("одеться", "надеть рубашку");
        manager.createEpic(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(epic), response.body());
    }
}