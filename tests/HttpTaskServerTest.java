import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import main.manager.HttpTaskManager;
import main.manager.TaskManager;
import main.server.HttpTaskServer;
import main.server.KVServer;
import main.service.LocalDateTimeTypeAdapter;
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
    static HttpTaskServer httpServer;
    static HttpTaskManager manager;
    static KVServer kvServer;

    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).create();


    @BeforeAll
    public static void init() throws IOException, InterruptedException {
        manager = new HttpTaskManager("http://localHost:8078/");
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpServer = new HttpTaskServer(manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void exit() {
        kvServer.stop();
    }

    @Test
    public void souldReturnTaskGet() {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task("одеться", "надеть рубашку", 1,
                LocalDateTime.of(2023, Month.FEBRUARY, 4, 10, 10));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .header("Content-Type", "application/json")
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
/*@Test
    public void*//*

}*/
