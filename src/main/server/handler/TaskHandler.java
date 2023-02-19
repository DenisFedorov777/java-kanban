package main.server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.FileBackedTasksManager;
import main.manager.LocalDateTimeTypeAdapter;
import main.manager.Managers;
import main.manager.TaskManager;
import main.server.HttpTaskServer;
import main.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private TaskManager manager;
    private Gson gson;

    public TaskHandler(TaskManager manager) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gson = gsonBuilder.create();
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET": {
                handleGet(exchange);
            }
            case "POST": {

            }
            case "DELETE": {

            }
            default:

        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Task> taski = manager.getTaskList();
        String taskJson = gson.toJson(taski);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(taskJson.getBytes());
        }
    }
}