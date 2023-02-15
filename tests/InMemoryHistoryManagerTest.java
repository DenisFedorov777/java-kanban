import main.manager.HistoryManager;
import main.manager.InMemoryHistoryManager;
import main.tasks.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    HistoryManager manager;
    private int id = 0;

    public int createId() {
        return ++id;
    }

    protected Task createTask() {
        return new Task("Простая задача", "Описание простой задачи",
                10, LocalDateTime.of(2023, Month.FEBRUARY, 10, 5, 0, 0));
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = createTask();
        int taskId1 = createId();
        task1.setId(taskId1);
        Task task2 = createTask();
        int taskId2 = createId();
        task2.setId(taskId2);
        Task task3 = createTask();
        int taskId3 = createId();
        task3.setId(taskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void shouldAddDouble1to1TasksToHistory() {// проверка дубля
        Task task1 = createTask();
        int taskId1 = createId();
        task1.setId(taskId1);
        Task task2 = createTask();
        task2.setId(taskId1);
        task1 = task2;
        manager.add(task1);
        manager.add(task2);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveTask() { // проверка дубля задач
        Task task1 = createTask();
        int taskId1 = createId();
        task1.setId(taskId1);
        Task task2 = createTask();
        int taskId2 = createId();
        task2.setId(taskId2);
        Task task3 = createTask();
        int taskId3 = createId();
        task3.setId(taskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.getId());
        assertEquals(List.of(task1, task3), manager.getHistory());
    }

    @Test
    public void shouldNotRemoveTaskWithBadId() {
        Task task = createTask();
        int taskId = createId();
        task.setId(taskId);
        manager.add(task);
        manager.remove(0);
        assertEquals(List.of(task), manager.getHistory());
    }

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task = createTask();
        int taskId = createId();
        task.setId(taskId);
        manager.add(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldHistoryIsEmpty() {
        Task task1 = createTask();
        int taskId1 = createId();
        task1.setId(taskId1);
        Task task2 = createTask();
        int taskId2 = createId();
        task2.setId(taskId2);
        Task task3 = createTask();
        int taskId3 = createId();
        task3.setId(taskId3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}