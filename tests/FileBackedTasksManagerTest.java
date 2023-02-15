import main.manager.FileBackedTasksManager;
import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public static File DATA_TEST = Paths.get("test.csv").toFile();

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(DATA_TEST);
        super.beforeEach();
    }

    @AfterEach
    void clearFile() {
        try {
            Files.delete(Path.of(DATA_TEST.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnEmptyListsAllTasksWhenCreateManagerFromEmptyFile() {
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(DATA_TEST);
        ArrayList<Epic> epics = new ArrayList<>(managerFromFile.getEpicList());
        ArrayList<Task> tasks = new ArrayList<>(managerFromFile.getTaskList());
        ArrayList<SubTask> subtasks = new ArrayList<>(managerFromFile.getSubTaskList());
        assertFalse(epics.isEmpty(), "При чтении из пустого файла список эпиков не пустой");
        assertFalse(subtasks.isEmpty(), "При чтении из пустого файла список подзадач не пустой");
        assertFalse(tasks.isEmpty(), "При чтении из пустого файла список задач не пустой");
    }

    @Test
    void shouldReturnLoadManagerFromFileWithOneEpicWithoutSubtasks() {
        manager.clearEpics();
        manager.clearSubtasks();
        manager.clearTask();
        manager.createEpic(emptyEpic);
        ArrayList<Epic> epics = new ArrayList<>(manager.getEpicList());
        ArrayList<Task> subtasks = new ArrayList<>(manager.getSubTaskList());
        assertFalse(epics.isEmpty(), "Список эпиков пустой");
        assertTrue(subtasks.isEmpty(), "Список подзадач не пустой");
        ArrayList<Epic> epicsFromFile = new ArrayList<>(manager.getEpicList());
        ArrayList<SubTask> subtasksFromFile = new ArrayList<>(manager.getSubTaskList());
        assertFalse(epicsFromFile.isEmpty(), "Список эпиков пустой");
        assertTrue(subtasksFromFile.isEmpty(), "Список подзадач не пустой");

    }

    @Test
    void shouldReturnLoadManagerFromFileWithEmptyHistory() {
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(DATA_TEST);
        ArrayList<Epic> epicsFromFile = new ArrayList<>(managerFromFile.getEpicList());
        ArrayList<SubTask> subtasksFromFile = new ArrayList<>(managerFromFile.getSubTaskList());
        ArrayList<Task> tasksFromFile = new ArrayList<>(managerFromFile.getTaskList());
        ArrayList<Task> historyFromFile = new ArrayList<>(managerFromFile.getHistory());
        assertFalse(epicsFromFile.isEmpty(), "Список эпиков пустой");
        assertFalse(subtasksFromFile.isEmpty(), "Список подзадач пустой");
        assertFalse(tasksFromFile.isEmpty(), "Список задач пустой");
        assertTrue(historyFromFile.isEmpty(), "История восстановленного менеджера не пустая");
    }

    @Test
    void shouldReturnLoadManagerFromFileWithNotEmptyHistory() {
        manager.getEpic(epicWithSubtasks.getId());
        ArrayList<Epic> epicsFromFile = new ArrayList<>(manager.getEpicList());
        ArrayList<SubTask> subtasksFromFile = new ArrayList<>(manager.getSubTaskList());
        ArrayList<Task> tasksFromFile = new ArrayList<>(manager.getTaskList());
        ArrayList<Task> historyFromFile = new ArrayList<>(manager.getHistory());
        assertFalse(epicsFromFile.isEmpty(),
                "При чтении из непустого файла список эпиков пустой");
        assertFalse(subtasksFromFile.isEmpty(),
                "При чтении из непустого файла список подзадач пустой");
        assertFalse(tasksFromFile.isEmpty(),
                "При чтении из непустого файла список подзадач пустой");
        assertFalse(historyFromFile.isEmpty(),
                "История восстановленного менеджера пустая");
    }
}