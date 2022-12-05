package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    // геттеры хэш-мап
    public ArrayList<Task> getTaskList() { // печать списка задач
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTaskList() { // печать списка подзадач
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpicList() { // печать списка эпика
        return new ArrayList<>(epics.values());
    }


    //методы создания
    public void createTask(Task task) {  //создали Таск
        task.setStatus("NEW");
        task.setId(++id);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) { //создали эпик
        epic.setStatus("NEW");
        epic.setId(++id);
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) { //создали сабтаск
        subTask.setStatus("NEW");
        subTask.setId(++id);
        epics.get(subTask.getEpicId()).getSubtaskList().add(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        setStatus(subTask.getEpicId());
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
        epics.get(epicId).getSubtaskList().remove((Integer) id);
        subTasks.remove(id);
        setStatus(epicId);
    }

    public void clearTask() { // очистили список Таск
        tasks.clear();
    }

    public Task getTask(int id) { // получение по идентификатору
        return tasks.get(id);
    }


    public void updateTask(Task task) { //обновление Task
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        setStatus(subTask.getEpicId());
    }

    public void updateEpic(Epic epic) {
        epic.setSubtaskList(epics.get(epic.getId()).getSubtaskList());
        epic.setStatus(epics.get(epic.getId()).getStatus());
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Task> getListSub(int id) { //получение списка подзадач одного эпика
        ArrayList<Task> listSubtask = new ArrayList<>();
        for (Integer i : epics.get(id).getSubtaskList()) {
            listSubtask.add(subTasks.get(i));
        }
        return listSubtask;
    }

    public void setStatus(int id) {
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

