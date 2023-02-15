import main.manager.TaskManager;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.SubTask;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static main.tasks.Status.DONE;
import static main.tasks.Status.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    protected Task task;
    protected Epic epicWithSubtasks;
    protected Epic emptyEpic;
    protected SubTask subtask1;
    protected SubTask subtask2;

    @BeforeEach
    void beforeEach() {
        task = new Task("Простая задача", "Описание простой задачи",
                10, LocalDateTime.of(2023, Month.FEBRUARY, 1, 10, 0, 0));
        manager.createTask(task);
        epicWithSubtasks = new Epic("сложный эпик", "Описание сложного эпика");
        manager.createEpic(epicWithSubtasks);
        emptyEpic = new Epic("простой эпик", "Описание простого эпика");
        manager.createEpic(emptyEpic);
        subtask1 = new SubTask("Сабтаск1", "Описание Subtask 1", 10,
                LocalDateTime.of(2023, Month.FEBRUARY, 2, 8, 0, 0), epicWithSubtasks.getId());
        manager.createSubTask(subtask1);
        subtask2 = new SubTask("Сабтаск2", "Описание Subtask 2", 45,
                LocalDateTime.of(2023, Month.FEBRUARY, 2, 12, 0, 0), epicWithSubtasks.getId());
        manager.createSubTask(subtask2);
    }

    @Test
    void shouldCreatedTaskWithStatusNew() {
        assertEquals(Status.NEW, task.getStatus(), "Статус новой задачи не NEW");
    }

    @Test
    void shouldCreateEpicWithStatusNew() {
        assertEquals(Status.NEW, epicWithSubtasks.getStatus(), "Статус только что созданного эпика не NEW");
    }

    @Test
    void shouldCreatedSubtaskWithStatusNew() {
        assertEquals(Status.NEW, subtask1.getStatus(), "Статус только что созданной подзадачи не NEW");
    }

    @Test
    void shouldEpicWithStatusNewAfterCreateSubtask() {
        assertEquals(Status.NEW, epicWithSubtasks.getStatus(),
                "После создания подзадачи со статусом NEW, статус эпика подзадачи не NEW");
    }

    @Test
    void shouldNotEmptyListTasksWithCreatedTask() {
        List<Task> allTasks = new ArrayList<>(manager.getTaskList());
        assertFalse(allTasks.isEmpty(),
                "Список всех задач пустой");
        assertTrue(allTasks.contains(task),
                "После создания задачи, список всех задач не содержит созданную задачу");
    }

    @Test
    void shouldNotEmptyListEpicsWithCreatedEpic() {
        List<Epic> allEpics = new ArrayList<>(manager.getEpicList());
        assertFalse(allEpics.isEmpty(),
                "Список всех эпиков пустой");
        assertTrue(allEpics.contains(epicWithSubtasks),
                "После создания эпика, список всех эпиков не содержит созданный эпик");
    }

    @Test
    void shouldNotEmptyListSubtasksWithCreatedSubtask() {
        List<SubTask> allSubtasks = new ArrayList<>(manager.getSubTaskList());
        assertFalse(allSubtasks.isEmpty(), "Список всех подзадач пустой");
        assertTrue(allSubtasks.contains(subtask1),
                "После создания подзадачи, список всех подзадач не содержит созданную подзадачу");
    }

    @Test
    void shouldNotEmptyListEpicsWithEpicCreatedSubtask() {
        List<Epic> allEpics = new ArrayList<>(manager.getEpicList());
        assertFalse(allEpics.isEmpty(), "Список всех эпиков пустой");
        assertTrue(allEpics.contains(epicWithSubtasks),
                "После создания подзадачи, список всех эпиков не содержит эпик подзадачи");
    }

    @Test
    void shouldReturnEmptyListTasksWithoutTasks() {
        manager.clearTask();
        Collection<Task> allTasks = manager.getTaskList();
        assertTrue(allTasks.isEmpty(), "Список всех задач не пустой");
    }

    @Test
    void shouldReturnEmptyListEpicsWithoutEpics() {
        manager.clearEpics();
        Collection<Epic> epicTasks = manager.getEpicList();
        assertTrue(epicTasks.isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void shouldReturnEmptyListSubtasksWithoutSubtasks() {
        manager.clearSubtasks();
        Collection<SubTask> allSubtasks = manager.getSubTaskList();
        assertTrue(allSubtasks.isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void shouldReturnEmptyListTasksAfterDeleteAllTasks() {
        assertFalse(manager.getTaskList().isEmpty(), "Список всех задач пустой");
        manager.clearTask();
        assertTrue(manager.getTaskList().isEmpty(), "Список всех задач не пустой");
    }

    @Test
    void shouldReturnEmptyListEpicsAfterDeleteAllEpics() {
        assertFalse(manager.getEpicList().isEmpty(), "Список всех эпиков пустой");
        manager.clearEpics();
        assertTrue(manager.getEpicList().isEmpty(), "Список всех эпиков не пустой");
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteAllEpics() {
        assertFalse(manager.getSubTaskList().isEmpty(), "Список всех подзадач пустой");
        manager.clearEpics();
        assertTrue(manager.getSubTaskList().isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteAllSubtasks() {
        assertFalse(manager.getSubTaskList().isEmpty(), "Список всех подзадач пустой");
        manager.clearSubtasks();
        assertTrue(manager.getSubTaskList().isEmpty(), "Список всех подзадач не пустой");
    }

    @Test
    void shouldReturnEmptyListEpicsAfterDeleteAllSubtasks() {
        assertFalse(manager.getSubTaskList().isEmpty(), "Список всех эпиков пустой");
        manager.clearSubtasks();
        assertFalse(manager.getEpicList().isEmpty(), "Список всех эпиков пустой");
    }

    @Test
    void shouldReturnTaskByCorrectId() {
        Task taskById = manager.getTask(task.getId());
        assertEquals(task, taskById, "Полученная задача не равна искомой задаче");
    }

    @Test
    void shouldReturnTaskByIncorrectId() {
        manager.getTaskList();
        assertNotEquals(task.getId()+5, task.getId(),
                "Задачи по id не равны");
    }

    @Test
    void shouldReturnEpicByCorrectId() {
        Epic epicById = manager.getEpic(epicWithSubtasks.getId());
        assertEquals(epicWithSubtasks, epicById, "Полученный эпик не равен искомому эпику");
    }

    @Test
    void shouldReturnEpicByIncorrectId() {
        manager.getEpicList();
        assertNotEquals(epicWithSubtasks.getId(), emptyEpic.getId(),
                "Задачи по id не равны");
    }

    @Test
    void shouldReturnSubtaskByCorrectId() {
        SubTask subtaskById = manager.getSubtask(subtask1.getId());
        assertEquals(subtask1, subtaskById, "Полученная подзадача не равна искомой подзадаче");
    }

    @Test
    void shouldReturnSubtaskByIncorrectId() {
        manager.getEpicList();
        assertNotEquals(subtask1.getId(), subtask1.getId()+5,
                "Задачи по id не равны");
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterCreateEpicWithoutSubtask() {
        manager.removeEpic(epicWithSubtasks.getId());
        ArrayList<Task> listSub = manager.getListSub(emptyEpic.getId());
        assertTrue(listSub.isEmpty(),
                "При создании эпика без подзадач, список подзадач, полученный по id эпика не пустой");
    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        subtask1.setStatus(IN_PROGRESS);
        manager.updateSubTask(subtask1);
        assertEquals(IN_PROGRESS, epicWithSubtasks.getStatus());
        assertEquals(IN_PROGRESS, subtask1.getStatus());
    }

    @Test
    void shouldReturnListWithoutDeletedTaskAfterDeleteTask() {
        manager.removeTask(task.getId());
        assertFalse(manager.getTaskList().contains(task),
                "После удаления задачи по id, список всех задач содержит удаленную задачу");
    }

    @Test
    void shouldReturnListWithoutDeletedEpicAfterDeleteEpic() {
        manager.removeEpic(epicWithSubtasks.getId());
        assertFalse(manager.getEpicList().contains(epicWithSubtasks),
                "После удаления эпика по id, список всех эпиков содержит удаленный эпик");
    }

    @Test
    void shouldReturnListWithoutDeletedSubtaskByEpicAfterDeleteEpic() {
        for (Task SubtaskId: manager.getListSub(epicWithSubtasks.getId())) {
            manager.removeSubTask(SubtaskId.getId());
        }
        assertFalse(manager.getSubTaskList().contains(subtask1),
                "Список всех подзадач содержит подзадачи удаленного эпика");
    }

    @Test
    void shouldReturnListWithoutDeletedSubtaskAfterDeleteSubtask() {
        manager.removeSubTask(subtask1.getId());
        assertFalse(manager.getSubTaskList().contains(subtask1),
                "Список всех подзадач содержит удаленную подзадачу");
    }

    @Test
    void shouldReturnListWithEpicAfterDeleteSubtask() {
        manager.removeSubTask(subtask1.getId());
        assertTrue(manager.getEpicList().contains(epicWithSubtasks),
                "Список всех эпиков не содержит эпик удаленной подзадачи");
    }

    @Test
    void shouldReturnUpdatedWithChangedStatusAfterUpdateTask() {
        task.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, manager.getTask(task.getId()).getStatus(),
                "Статус подзадачи не IN_PROGRESS");
    }

    @Test
    void shouldReturnUpdatedEpicWithChangedStatusAfterUpdateEpic() {
        epicWithSubtasks.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, manager.getEpic(epicWithSubtasks.getId()).getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @Test
    void shouldReturnUpdatedSubtaskWithChangedStatusAfterUpdateSubtask() {
        subtask1.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, manager.getSubtask(subtask1.getId()).getStatus(),
                "Статус подзадачи не IN_PROGRESS");
    }

    @Test
    void shouldReturnUpdatedEpicWithChangedStatusAfterUpdateSubtask() {
        subtask1.setStatus(IN_PROGRESS);
        manager.updateSubTask(subtask1);
        assertEquals(IN_PROGRESS, epicWithSubtasks.getStatus(),
                "Статус эпика  не IN_PROGRESS");
    }

    @Test
    void shouldReturnUpdatedEpicWithChangedStatusAfterUpdateAllSubtasks() { //обновление статуса эпика на DONE
        subtask1.setStatus(DONE);
        subtask2.setStatus(DONE);
        manager.updateSubTask(subtask1);
        manager.updateSubTask(subtask2);
        assertEquals(DONE, epicWithSubtasks.getStatus(),
                "Статус эпика  не DONE");
    }

    @Test
    void shouldReturnHistoryListWithSize3AfterViewThreeTasks() {
        manager.getEpic(epicWithSubtasks.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        assertFalse(manager.getHistory().isEmpty(), "История пустая");
        assertEquals(3, manager.getHistory().size(), "Размер истории после просмотра трех задач не равен 3");
    }

    @Test
    void shouldThrowWhenAddTwoTaskWithSameStartTimeAndDuration() {
        Task task1 = new Task("Дубль простой задачи", "Описание простой задачи",
                10, LocalDateTime.of(2023, Month.FEBRUARY, 1, 10, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> manager.createTask(task1),
                "Ошибка, пересечение задач по времени");
    }

    @Test
    void shouldReturnCorrectPrioritizedTasks() {
        List<Task> prioritizedTasks = new ArrayList<>(manager.getListOfPriority());
        assertEquals(task, prioritizedTasks.get(0),
                "Первая задача по приоритету не равна первой задаче в приоритетном списке");
        assertEquals(subtask1, prioritizedTasks.get(1),
                "Вторая задача по приоритету не равна второй задаче в приоритетном списке");
    }
}