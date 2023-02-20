package main.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import main.manager.Managers;
import main.manager.TaskManager;
import main.server.handler.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final Gson gson;
    private final HttpServer httpServer;

    private TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        gson = new Gson();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(manager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(manager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(manager));
        httpServer.createContext("/tasks/subtask/epic/", new SubtaskByEpicHandler(manager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(manager));
        httpServer.createContext("/tasks/", new AllTasksHandler(manager));
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер запущен на порту" + PORT);
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("Сервер остановлен");
    }
}
