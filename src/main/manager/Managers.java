package main.manager;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("resources/DataFile.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getHttpDefault() {
        return new HttpTaskManager();
    }
}