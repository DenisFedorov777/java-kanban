package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> getTaskList() { // печать списка задач
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTaskList() { // печать списка подзадач
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpicList() { // печать списка эпиков
        return new ArrayList<>(epics.values());
    }

    public void createTask(Task task) {  //создали Таск
        task.setId(++id);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) { //создали эпик
        epic.setId(++id);
        epics.put(epic.getId(), epic);
    }

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

    public void removeTask(int id) { //удалили по id
        tasks.remove(id);
    }

    public void removeEpic(int id) { //удалить эпик
        for (Integer subId : epics.get(id).getSubtaskList()) {
            subTasks.remove(subId);
        }
        epics.remove(id);
    }

    public void removeSubTask(int id) { //удалить сабтаск
        int epicId = subTasks.get(id).getEpicId();
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            epics.get(epicId).getSubtaskList().remove((Integer) id);
            subTasks.remove(id);
            setStatus(epicId);
        } else {
            System.out.println("Подзадачи с таким id не найден");
        }
    }

    public void clearTask() { // очистили список Таск
        tasks.clear();
    }

    public Task getTask(int id) { // получение по идентификатору
        return tasks.get(id);
    }

    public void updateTask(Task task) { //обновление Task
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с таким id не найдена");
        }
    }

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

    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epic.setSubtaskList(epics.get(epic.getId()).getSubtaskList());
            epic.setStatus(epics.get(epic.getId()).getStatus());
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с таким id не найден");
        }
    }

    public ArrayList<Task> getListSub(int id) { //получение списка подзадач одного эпика
        ArrayList<Task> listSubtask = new ArrayList<>();
        for (Integer i : epics.get(id).getSubtaskList()) {
            listSubtask.add(subTasks.get(i));
        }
        return listSubtask;
    }

    private void setStatus(int id) {
        int doneCount = 0;
        int newCount = 0;
        for (Integer sId : epics.get(id).getSubtaskList()) {
            if (subTasks.get(sId).getStatus().equals("NEW")) {
                newCount++;
            } else if (subTasks.get(sId).getStatus().equals("DONE")) {
                doneCount++;
            }
        }
        if (epics.get(id).getSubtaskList().isEmpty() || newCount == epics.get(id).getSubtaskList().size()) {
            epics.get(id).setStatus("NEW");
        } else if (doneCount == epics.get(id).getSubtaskList().size()) {
            epics.get(id).setStatus("DONE");
        } else {
            epics.get(id).setStatus("IN_PROGRESS");
        }
    }
}