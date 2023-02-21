package main.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import main.manager.TaskManager;
import main.server.handler.AllTasksHandler;
import main.server.handler.EpicHandler;
import main.server.handler.SubtaskHandler;
import main.server.handler.TaskHandler;
import main.service.LocalDateTimeTypeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();


    private final HttpServer httpServer;

    private TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(manager, gson));
        httpServer.createContext("/tasks/epic", new EpicHandler(manager, gson));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(manager, gson));
        httpServer.createContext("/tasks/", new AllTasksHandler(manager, gson));
        httpServer.start();
        System.out.println("Сервер запущен на порту" + PORT);
    }

    public void stop() {
        httpServer.stop(0);
    }
}