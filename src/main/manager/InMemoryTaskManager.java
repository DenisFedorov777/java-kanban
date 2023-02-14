package main.manager;

import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import static main.tasks.Status.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    private final Set<Task> listOfPriority = new TreeSet<>((task1, task2) -> {
        if ((task1.getStartTime() != null) && (task2.getStartTime() != null)) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        } else if (task1.getStartTime() == null) {
            return 1;
        } else if (null == task2.getStartTime()) {
            return -1;
        }else {
            return 0;
        }
    });

    public Set<Task> getListOfPriority() {
        return listOfPriority;
    }

    @Override
    public List<Task> getTaskList() { // печать списка задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubTaskList() { // печать списка подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getEpicList() { // печать списка эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(Task task) {
        if(checkIntersection.test(task)) {
            throw new IllegalArgumentException("Ошибка, пересечение задач по времени");
        }
        task.setId(++id);
        tasks.put(task.getId(), task);
        listOfPriority.add(task);
        task.setStatus(NEW);
    }

    @Override
    public void createEpic(Epic epic) { //создали эпик
        if(!epics.containsKey(epic.getId())) {
            epic.setId(++id);
            epics.put(epic.getId(), epic);
            epic.setStatus(NEW);
        }
    }

    @Override
    public void createSubTask(SubTask subTask) { //создали сабтаск
        if(checkIntersection.test(subTask)) {
            throw new IllegalArgumentException("Ошибка, пересечение подзадач по времени");
        }
        subTask.setId(++id);
        Integer epicId = subTask.getEpicId();
        listOfPriority.add(subTask);
        subTask.setStatus(NEW);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            changeStatus(epicId);
            setEpicDuration(epicId);
            setEpicStart(epicId);
            epic.getSubtaskList().add(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
        } else {
            System.out.println("Эпик с таким id не найден");
        }
    }

    @Override
    public void removeTask(int id) { //удалили по id
        if(tasks.get(id) != null) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Такой задачи для удаления не существует");
        }
    }

    @Override
    public void removeEpic(int id) { //удалить эпик
        if(epics.get(id) != null) {
            for (Integer subId : epics.get(id).getSubtaskList()) {
                subTasks.remove(subId);
                historyManager.remove(subId);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Такого эпика не существует");
        }
    }

    @Override
    public void removeSubTask(int id) { //удалить сабтаск
        int epicId = subTasks.get(id).getEpicId();
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            epics.get(epicId).getSubtaskList().remove((Integer) id);
            subTasks.remove(id);
            changeStatus(epicId);
            setEpicDuration(epicId);
            setEpicStart(epicId);
            historyManager.remove(id);
        } else {
            System.out.println("Подзадачи с таким id не найден");
        }
    }

    @Override
    public void clearTask() { // очистили список Таск
        for(int id: tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for(int id: subTasks.keySet()) {
            historyManager.remove(id);
        }
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subtasks = epic.getSubtaskList();
            subtasks.clear();
            epic.setSubtaskList(subtasks);
        }
        subTasks.clear();
    }

    @Override
    public void clearEpics() { //удалить все эпики
        for(int id: epics.keySet()) {
            for(int subId: epics.get(id).getSubtaskList()) {
                historyManager.remove(subId);
            }
            historyManager.remove(id);
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public Task getTask(int id) { // получение по идентификатору
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id)); //записываем в список истории
        } else {
            System.out.println("Такой задачи не существует");
        }
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if(epics.get(id) != null) {
            historyManager.add(epics.get(id));
        } else {
            System.out.println("Такого эпика не существует");
        }
        return epics.get(id);
    }

    @Override
    public SubTask getSubtask(int id) {
        if(subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
        } else {
            System.out.println("Такой подзадачи не существует");
        }
        return subTasks.get(id);
    }

    @Override
    public void updateTask(Task task) { //обновление Task
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с таким id не найдена");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int subTaskId = subTask.getId();
        int epicId = subTasks.get(subTaskId).getEpicId();
        if (subTasks.get(subTaskId) != null) {
            subTask.setEpicId(epicId);
            setEpicDuration(epicId);
            setEpicStart(epicId);
            subTasks.put(subTaskId, subTask);
            changeStatus(epicId);
        } else {
            System.out.println("Подзадача с таким id не найдена");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epic.setSubtaskList(epics.get(epic.getId()).getSubtaskList());
            epic.setStatus(epics.get(epic.getId()).getStatus());
            setEpicStart(epic.getId());
            setEpicDuration(epic.getId());
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с таким id не найден");
        }
    }

    @Override
    public ArrayList<Task> getListSub(int id) {
        ArrayList<Task> listSubtask = new ArrayList<>();
        for (Integer i : epics.get(id).getSubtaskList()) {
            listSubtask.add(subTasks.get(i));
        }
        return listSubtask;
    }

    private void changeStatus(int id) {
        int doneCount = 0;
        int newCount = 0;
        for (Integer sId : epics.get(id).getSubtaskList()) {
            if (subTasks.get(sId).getStatus() == NEW) {
                newCount++;
            } else if (subTasks.get(sId).getStatus() == DONE) {
                doneCount++;
            }
        }
        if (epics.get(id).getSubtaskList().isEmpty() || newCount == epics.get(id).getSubtaskList().size()) {
            epics.get(id).setStatus(NEW);
        } else if (doneCount == epics.get(id).getSubtaskList().size()) {
            epics.get(id).setStatus(DONE);
        } else {
            epics.get(id).setStatus(IN_PROGRESS);
        }
    }

    private void setEpicDuration(Integer id) {
        long durationEpic = 0;
        for (Integer SubtaskId : epics.get(id).getSubtaskList()) {
            SubTask subTask = subTasks.get(SubtaskId);
            if (subTask != null) {
                durationEpic += subTask.getDuration();
            }
        }
        epics.get(id).setDuration(durationEpic);
    }

    private LocalDateTime setEpicStart(Integer id) {
        Epic epic = epics.get(id);
        for (Integer subtaskId : epic.getSubtaskList()) {
            SubTask subTask = subTasks.get(subtaskId);
            if (epic.getStartTime() == null ||subTask.getStartTime().isBefore(epic.getStartTime())) {
                epic.setStartTime(subTask.getStartTime());
            }
        }
        return epic.getStartTime();
    }

    private final Predicate<Task> checkIntersection = newTask -> { //проверка пересечения задач
        LocalDateTime newTaskStart = newTask.getStartTime();
        LocalDateTime newTaskFinish = newTask.getEndTime();
        for (Task task : listOfPriority) {
            LocalDateTime oldTaskStart = task.getStartTime();
            LocalDateTime oldTaskEnd = task.getEndTime();
            if (newTaskStart.isBefore(oldTaskStart) && newTaskFinish.isAfter(oldTaskStart)) {
                return true;
            }
            if (newTaskStart.isBefore(oldTaskEnd) && newTaskFinish.isAfter(oldTaskEnd)) {
                return true;
            }
            if (newTaskStart.isEqual(oldTaskStart) && newTaskFinish.isBefore(oldTaskEnd)) {
                return true;
            }
            if (newTaskStart.isEqual(oldTaskStart) && newTaskFinish.isEqual(oldTaskEnd)) {
                return true;
            }
        }
        return false;
    };
}