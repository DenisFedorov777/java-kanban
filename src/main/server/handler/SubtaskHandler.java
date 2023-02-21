package main.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                handleAllSubtasksGet(exchange);
            }
            case "POST": {
                handleTaskPOST(exchange);
            }
            case "DELETE": {
                if(exchange.getRequestURI().getQuery() == null) {
                    deleteSubtasks(exchange);
                } else {
                    handleSubtaskDelete(exchange);
                }
            }
            default:
                throw new RuntimeException();
        }
    }

    private int getSubtaskId(HttpExchange exchange) {
        return Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
    }

    private void handleAllSubtasksGet(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getQuery() != null) {
            SubTask task = manager.getSubtask(getSubtaskId(exchange));
            if(task == null) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(("Задачи с таким " + getSubtaskId(exchange) + " не существует.").getBytes());
                }
                exchange.close();
                return;
            }
            String taskJson = gson.toJson(task);
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(taskJson.getBytes());
            }
            exchange.close();
            return;
        }
        List<SubTask> subtaskList = manager.getSubTaskList();
        String taskJson = gson.toJson(subtaskList);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(taskJson.getBytes());
        }
        exchange.close();
    }

    private void handleTaskPOST(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String path = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        SubTask subtask = gson.fromJson(path, SubTask.class);
        if(exchange.getRequestURI().getQuery() != null) {
            for (SubTask sub : manager.getSubTaskList()) {
                if (sub.getId() == getSubtaskId(exchange)) {
                    manager.updateSubTask(sub);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(("Задача  обновлена " + getSubtaskId(exchange)).getBytes());
                    }
                    exchange.close();
                    return;
                }
            }
        }
        manager.createSubTask(subtask);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Задача успешно создана ".getBytes());
        }
        exchange.close();
    }

    public void deleteSubtasks(HttpExchange exchange) throws IOException {
        manager.clearSubtasks();
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(("Список задач очищен ".getBytes()));
        }
        exchange.close();
    }

    private void handleSubtaskDelete(HttpExchange exchange) throws IOException {
        for(SubTask subTask: manager.getSubTaskList()) {
            if(subTask.getId() == getSubtaskId(exchange)) {
                manager.removeSubTask(getSubtaskId(exchange));
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(("Задача удалена " + getSubtaskId(exchange)).getBytes());
                }
                exchange.close();
                return;
            }
        }

        exchange.sendResponseHeaders(404, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(("Задача не найдена " + getSubtaskId(exchange)).getBytes());
        }
        exchange.close();
    }
}