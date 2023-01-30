package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static service.Status.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id;
    final protected Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

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
    public void createTask(Task task) {  //создали Таск
        task.setId(++id);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) { //создали эпик
        epic.setId(++id);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) { //создали сабтаск
        subTask.setId(++id);
        Integer epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.getSubtaskList().add(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            setStatus(epicId);
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
            setStatus(epicId);
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
            System.out.println("Такой зачачи не существует");
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
            subTasks.put(subTaskId, subTask);
            setStatus(epicId);
        } else {
            System.out.println("Подзадача с таким id не найдена");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epic.setSubtaskList(epics.get(epic.getId()).getSubtaskList());
            epic.setStatus(epics.get(epic.getId()).getStatus());
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с таким id не найден");
        }
    }

    @Override
    public ArrayList<Task> getListSub(int id) { //получение списка подзадач одного эпика
        ArrayList<Task> listSubtask = new ArrayList<>();
        for (Integer i : epics.get(id).getSubtaskList()) {
            listSubtask.add(subTasks.get(i));
        }
        return listSubtask;
    }

    protected void setStatus(int id) {
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
}