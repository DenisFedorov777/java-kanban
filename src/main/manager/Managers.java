package main.manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("resources/DataFile.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}