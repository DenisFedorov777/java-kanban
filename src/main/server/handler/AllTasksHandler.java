package main.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Epic;
import main.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class AllTasksHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public AllTasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                handleAllGet(exchange);
            }
            case "DELETE": {
                handleEpicDELETE(exchange);
            }
            default:

        }
    }

    private void handleAllGet(HttpExchange exchange) throws IOException {
        List<Task> AllTasks = manager.getAll();
        String taskJson = gson.toJson(AllTasks);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(taskJson.getBytes());
        }
    }

    private void handleEpicDELETE(HttpExchange exchange) throws IOException {
        manager.setAllTasks();
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Все задачи удалены успешно".getBytes());
        }
    }
}
