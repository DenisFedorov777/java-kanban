import main.manager.FileBackedTasksManager;

import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public static final File DATA_TEST = new File("test.csv");

    @TempDir
    private Path directory;

    @BeforeEach
    public void beforeEach() {
        try {
            File file = Files.createFile(directory.resolve(DATA_TEST.toPath())).toFile();
            manager = FileBackedTasksManager.loadFromFile(file);
            super.beforeEach();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    @Test
    void shouldReturnEmptyListsAllTasksWhenCreateManagerFromEmptyFile() {
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(DATA_TEST);
        ArrayList<Epic> epics = new ArrayList<>(managerFromFile.getEpicList());
        ArrayList<Task> tasks = new ArrayList<>(managerFromFile.getTaskList());
        ArrayList<SubTask> subtasks = new ArrayList<>(managerFromFile.getSubTaskList());
        assertTrue(epics.isEmpty(), "При чтении из пустого файла список эпиков не пустой");
        assertTrue(subtasks.isEmpty(), "При чтении из пустого файла список подзадач не пустой");
        assertTrue(tasks.isEmpty(), "При чтении из пустого файла список задач не пустой");
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
        assertTrue(epicsFromFile.isEmpty(), "Список эпиков пустой");
        assertTrue(subtasksFromFile.isEmpty(), "Список подзадач пустой");
        assertTrue(tasksFromFile.isEmpty(), "Список задач пустой");
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