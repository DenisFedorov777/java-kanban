package main.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.server.KVClient;
import main.service.LocalDateTimeTypeAdapter;
import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {

    KVClient kvClient;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).create();

    public HttpTaskManager(String path) throws IOException, InterruptedException {
        super(null);
        kvClient = new KVClient(path);
    }

    public KVClient getKvClient() {
        return kvClient;
    }

    public void setKvClient(KVClient kvClient) {
        this.kvClient = kvClient;
    }

    public void load() {
        String taskJson = kvClient.get("task/");
        String epicJson = kvClient.get("epic/");
        String subtaskJson = kvClient.get("subtask/");
        String historyJson = kvClient.get("history/");

        Type typeTask = new TypeToken<Set<Task>>() {}.getType();
        Set<Task> tasks = gson.fromJson(taskJson, typeTask);
        Type typeEpic = new TypeToken<Set<Epic>>() {
        }.getType();
        Set<Epic> epics = gson.fromJson(epicJson, typeEpic);
        Type typeSubtask = new TypeToken<Set<SubTask>>() {
        }.getType();
        Set<SubTask> subtasks = gson.fromJson(subtaskJson, typeSubtask);
        List<Integer> history = Formatter.historyFromString(historyJson);

        if (tasks != null) {
            for (Task task : tasks) {
                this.tasks.put(task.getId(), task);
                listOfPriority.add(task);
            }
        }
        if (epics != null) {
            for (Epic epic : epics) {
                this.epics.put(epic.getId(), epic);
            }
        }
        if (subtasks != null) {
            for (SubTask subtask : subtasks) {
                this.subTasks.put(subtask.getId(), subtask);
                listOfPriority.add(subtask);
            }
        }
        if(!history.isEmpty()) {
            for (Integer taskId : history) {
                addToHistory(taskId);
            }
        }
    }

    @Override
    public void save() {
        Type typeTask = new TypeToken<Set<Task>>() {}.getType();
        String taskToJson = gson.toJson(tasks.values(), typeTask);
        String epicToJson = gson.toJson(epics.values(), typeTask);
        String subtaskToJson = gson.toJson(subTasks.values(), typeTask);
        String historyToJson = Formatter.historyToString(historyManager);
        kvClient.put("task/", taskToJson);
        kvClient.put("epic/", epicToJson);
        kvClient.put("subtask/", subtaskToJson);
        kvClient.put("history/", historyToJson);
    }
}