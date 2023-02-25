import main.manager.HttpTaskManager;
import main.server.KVServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static KVServer kvServer;

    @BeforeAll
    public static void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }
    @BeforeEach
    public void beforeEach() {
        try {
            manager = new HttpTaskManager("http://localhost:8078/");
            super.beforeEach();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldReturnAllListsTasksSaveToLoadFromServer() throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078/");
        httpTaskManager.setKvClient(manager.getKvClient());
        httpTaskManager.load();
        assertEquals(new ArrayList<>(List.of(task)), httpTaskManager.getTaskList(), "Задачи не равны");
        assertEquals(new ArrayList<>(List.of(epicWithSubtasks, emptyEpic)), httpTaskManager.getEpicList(), "Эпики не равны");
        assertEquals(new ArrayList<>(List.of(subtask1, subtask2)), httpTaskManager.getSubTaskList());
        assertIterableEquals(new ArrayList<>(List.of(task, subtask1, subtask2)), httpTaskManager.getListOfPriority());
    }
}