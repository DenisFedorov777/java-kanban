package main.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HistoryHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("GET")) {
            String path = gson.toJson(manager.getHistory());
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(path.getBytes());
            }
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(405, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Метод не GET ".getBytes());
        }
        exchange.close();
    }
}