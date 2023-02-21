package main.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Epic;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
        Epic epic = gson.fromJson(path, Epic.class);
        if(exchange.getRequestURI().getQuery() != null) {
            for (Epic epic1 : manager.getEpicList()) {
                if (epic1.getId() == getEpicId(exchange)) {
                    manager.updateEpic(epic1);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(("Задача  обновлена " + getEpicId(exchange)).getBytes());
                    }
                    exchange.close();
                    return;
                }
            }
        }
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