package main.server.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Status;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET": {
                handleAllTasksGet(exchange);
            }
            case "POST": {
                handleTaskPOST(exchange);
            }
            case "DELETE": {
                if (exchange.getRequestURI().getQuery() == null) {
                    deleteTasks(exchange);
                } else {
                    handleTaskDelete(exchange);
                }
            }
            default:
                throw new RuntimeException();
        }
    }

    private int getTaskId(HttpExchange exchange) {
        return Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
    }

    private void handleAllTasksGet(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() != null) {
            Task task = manager.getTask(getTaskId(exchange));
            if (task == null) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(("Задачи с таким " + getTaskId(exchange) + " не существует.").getBytes());
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
        List<Task> taskList = manager.getTaskList();
        String taskJson = gson.toJson(taskList);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(taskJson.getBytes());
        }
        exchange.close();
    }

    private void handleTaskPOST(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String path = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(path);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd--MM--yyyy, HH:mm");
        System.out.println(jsonObject.get("startTime").getAsString());
        Task t = new Task(jsonObject.get("name").getAsString()
                , jsonObject.get("description").getAsString()
                , Status.valueOf(jsonObject.get("status").getAsString())
                , jsonObject.get("duration").getAsLong()
                , LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter));
        if (exchange.getRequestURI().getQuery() != null) {
            for (Task taska : manager.getTaskList()) {
                if (taska.getId() == getTaskId(exchange)) {
                    manager.updateTask(t);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(("Задача  обновлена " + getTaskId(exchange)).getBytes());
                    }
                    exchange.close();
                    return;
                }
            }
        }
        manager.createTask(t);
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Задача успешно создана ".getBytes());
        }
        exchange.close();
    }

    public void deleteTasks(HttpExchange exchange) throws IOException {
        manager.clearTask();
        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(("Список задач очищен ".getBytes()));
        }
        exchange.close();
    }

    private void handleTaskDelete(HttpExchange exchange) throws IOException {
        for (Task taska : manager.getTaskList()) {
            if (taska.getId() == getTaskId(exchange)) {
                manager.removeTask(getTaskId(exchange));
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(("Задача удалена " + getTaskId(exchange)).getBytes());
                }
                exchange.close();
                return;
            }
        }
        exchange.sendResponseHeaders(404, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(("Задача не найдена " + getTaskId(exchange)).getBytes());
        }
        exchange.close();
    }

    public static class TaskSerializer implements JsonSerializer<Task> {

        @Override
        public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject result = new JsonObject();
            result.addProperty("id", task.getId());
            result.addProperty("name", task.getName());
            result.addProperty("description", task.getDescription());
            result.addProperty("duration", task.getDuration());
            result.add("startTime", jsonSerializationContext.serialize(task.getStartTime()));
            result.addProperty("status", task.getStatus().toString());
            return result;
        }
    }
    public class TaskDeserializer implements JsonDeserializer<Task> {

        @Override
        public Task deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            long duration = jsonObject.get("duration").getAsLong();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd--MM--yyyy, HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter);
            Task task = new Task(name, description, duration, startTime);
            if(jsonObject.has("id"))
                task.setId(jsonObject.get("id").getAsInt());
            if(jsonObject.has("duration"))
                task.setDuration(jsonObject.get("duration").getAsLong());
            if(jsonObject.has("startTime")) {
                task.setStartTime(jsonDeserializationContext
                        .deserialize(jsonObject.get("startTime"), LocalDateTime.class));
            }
            if(jsonObject.has("status"))
                task.setStatus(Status.valueOf(jsonObject.get("status").toString()));
            return task;
        }
    }
}