package manager;

import java.io.File;

public class Managers {

    /*public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }*/

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(new File("resourses/DataFile.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}