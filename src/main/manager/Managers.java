package main.manager;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefaultHttpServer() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078/");
    }

    /*public static TaskManager getDefaultHttpServer() {
        return new FileBackedTasksManager(new File("resources/DataFile.csv"));
    }*/

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}