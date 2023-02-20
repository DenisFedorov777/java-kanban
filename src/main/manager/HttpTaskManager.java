package main.manager;

import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    private final HttpClient kvServerClient = HttpClient.newHttpClient();
    private final String API_TOKEN;

    public HttpTaskManager() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/register");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "applicatoin/json")
                .uri(uri)
                .build();

        HttpResponse<String> apiToken = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = apiToken.body();
    }

    public void save() {
        URI taskUri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + API_TOKEN);
        URI epicUri = URI.create("http://localhost:8078/save/epics?API_TOKEN=" + API_TOKEN);
        URI subtaskUri = URI.create("http://localhost:8078/save/subtasks?API_TOKEN=" + API_TOKEN);
        URI historyUri = URI.create("http://localhost:8078/save/history?API_TOKEN=" + API_TOKEN);
    }

    public void loadFromFile() {
        URI taskUri = URI.create("http://localhost:8078/load/tasks?API_TOKEN=" + API_TOKEN);
        URI epicUri = URI.create("http://localhost:8078/load/epics?API_TOKEN=" + API_TOKEN);
        URI subtaskUri = URI.create("http://localhost:8078/load/subtasks?API_TOKEN=" + API_TOKEN);
        URI historyUri = URI.create("http://localhost:8078/load/history?API_TOKEN=" + API_TOKEN);
        //делаем запрос к rv-server, получаем от него задачи в формате json, складываем в хэш-мап.
    }

    @Override
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public List<Task> getTaskList() {
        return null;
    }

    @Override
    public List<SubTask> getSubTaskList() {
        return null;
    }

    @Override
    public List<Epic> getEpicList() {
        return null;
    }

    @Override
    public void createTask(Task task) {

    }

    @Override
    public void createEpic(Epic epic) {

    }

    @Override
    public void createSubTask(SubTask subTask) {

    }

    @Override
    public void removeTask(int id) {

    }

    @Override
    public void removeEpic(int id) {

    }

    @Override
    public void removeSubTask(int id) {

    }

    @Override
    public void clearTask() {

    }

    @Override
    public void clearSubtasks() {

    }

    @Override
    public void clearEpics() {

    }

    @Override
    public Task getTask(int id) {
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        return null;
    }

    @Override
    public SubTask getSubtask(int id) {
        return null;
    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void updateSubTask(SubTask subTask) {

    }

    @Override
    public void updateEpic(Epic epic) {

    }

    @Override
    public ArrayList<Task> getListSub(int id) {
        return null;
    }

    @Override
    public Set<Task> getListOfPriority() {
        return null;
    }
}