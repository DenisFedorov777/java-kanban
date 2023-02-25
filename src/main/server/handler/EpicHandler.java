package main.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EpicHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public EpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                handleAllEpicsGet(exchange);
            }
            case "POST": {
                handleEpicPOST(exchange);
            }
            case "DELETE": {
                if(exchange.getRequestURI().getQuery() == null) {
                    deleteEpics(exchange);
                } else {
                    handleEpicDelete(exchange);
                }
            }
            default:
                throw new RuntimeException();
        }
    }

    private int getEpicId(HttpExchange exchange) {
        return Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
    }

    private void handleAllEpicsGet(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getQuery() != null) {
            Epic epic = manager.getEpic(getEpicId(exchange));
            if(epic == null) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(("Задачи с таким " + getEpicId(exchange) + " не существует.").getBytes());
                }
                exchange.close();
                return;
            }
            String taskJson = gson.toJson(epic);
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(taskJson.getBytes());
            }
            exchange.close();
            return;
        }
        List<Epic> epicList = manager.getEpicList();
        String taskJson = gson.toJson(epicList);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(taskJson.getBytes());
        }
        exchange.close();
    }

    private void handleEpicPOST(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String path = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String query = exchange.getRequestURI().getQuery();
        if(query != null) {
            String[] array = query.split("=");
            int epicId = Integer.parseInt(array[1]);
            Epic epicJson = manager.getEpic(epicId);
            manager.updateEpic(epicJson);
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Эпик успешно создана ".getBytes());
            }
            exchange.close();
            return;
        }
        JsonElement jsonElement = JsonParser.parseString(path);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd--MM--yyyy, HH:mm");
        Epic epic = new Epic(jsonObject.get("id").getAsInt()
                ,jsonObject.get("name").getAsString()
                , jsonObject.get("description").getAsString()
                , jsonObject.get("duration").getAsLong()
                , LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter)
                , Status.valueOf(jsonObject.get("status").getAsString()));
        manager.createEpic(epic);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Эпик успешно создана ".getBytes());
        }
        exchange.close();
    }

    public void deleteEpics(HttpExchange exchange) throws IOException {
        manager.clearEpics();
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(("Список эпиков очищен ".getBytes()));
        }
        exchange.close();
    }

    private void handleEpicDelete(HttpExchange exchange) throws IOException {
        for(Epic epic: manager.getEpicList()) {
            if(epic.getId() == getEpicId(exchange)) {
                manager.removeEpic(getEpicId(exchange));
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(("Эпик удален " + getEpicId(exchange)).getBytes());
                }
                exchange.close();
                return;
            }
        }

        exchange.sendResponseHeaders(404, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(("Эпик не найден " + getEpicId(exchange)).getBytes());
        }
        exchange.close();
    }
}