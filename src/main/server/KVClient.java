package main.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    String path;
    HttpClient httpClient = HttpClient.newHttpClient();
    String Api_Token;

    public KVClient(String path) throws IOException, InterruptedException {
        this.path = path;
        Api_Token = registered(path);
    }

    private String registered(String str) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-Type", "application/json")
                .uri(URI.create(str + "register"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void put(String str, String str1) throws IOException, InterruptedException {
        URI uri = URI.create(path + "save/" + str + "?API_TOKEN=" + Api_Token);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(str1)).uri(uri).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String get(String str) throws IOException, InterruptedException {
        URI uri = URI.create(path + "load/" + str + "?API_TOKEN=" + Api_Token);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}