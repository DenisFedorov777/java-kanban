package main.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.server.KVClient;
import main.service.LocalDateTimeTypeAdapter;
import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {

    KVClient kvClient;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).create();

    public HttpTaskManager(String str) throws IOException, InterruptedException {
        super(null);
        KVClient kvClient = new KVClient(str);
    }

    public void load() throws IOException, InterruptedException {
        String taskJson = kvClient.get("task/");
        String epicJson = kvClient.get("epic/");
        String subtaskJson = kvClient.get("subtask/");
        String historyJson = kvClient.get("history/");

        Type typeTask = new TypeToken<Set<Task>>() {}.getType();
        Set<Task> tasks = gson.fromJson(taskJson, typeTask);
        Type typeEpic = new TypeToken<Set<Epic>>() {}.getType();
        Set<Epic> epics = gson.fromJson(epicJson, typeEpic);
        Type typeSubtask = new TypeToken<Set<SubTask>>() {}.getType();
        Set<SubTask> subtasks = gson.fromJson(subtaskJson, typeSubtask);
        Set<Task> history = gson.fromJson(historyJson, typeTask);

        if(tasks != null) {
            for(Task task: tasks) {
                this.tasks.put(task.getId(), task);
                listOfPriority.add(task);
            }
        }
        if(epics != null) {
            for(Epic epic: epics) {
                this.epics.put(epic.getId(), epic);
                listOfPriority.add(epic);
            }
        }
        if(subtasks != null) {
            for(SubTask subtask: subtasks) {
                this.subTasks.put(subtask.getId(), subtask);
                listOfPriority.add(subtask);
            }
        }
        if(history != null) {
            for(Task task: history) {
                this.getHistory().add(task);
                listOfPriority.add(task);
            }
        }
    }

    public void save() {
    }
}