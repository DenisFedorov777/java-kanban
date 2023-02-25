package main.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private final String path;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_TOKEN;

    public KVClient(String path) throws IOException, InterruptedException {
        this.path = path;
        API_TOKEN = registered(path);
    }

    private String registered(String str) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-Type", "application/json")
                .uri(URI.create(str + "register"))
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    public void put(String key, String value) {
        URI uri = URI.create(path + "save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(value)).uri(uri).build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String get(String key){
        HttpResponse<String> response = null;
        URI uri = URI.create(path + "load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }
}