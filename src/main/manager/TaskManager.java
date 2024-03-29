package main.manager;

import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getHistory();

    List<Task> getTaskList(); // печать списка задач

    List<SubTask> getSubTaskList(); // печать списка подзадач

    List<Epic> getEpicList(); // печать списка эпиков

    void createTask(Task task); //создали Таск

    void createEpic(Epic epic); //создали эпик

    void createSubTask(SubTask subTask); //создали сабтаск

    void removeTask(int id); //удалили по id

    void removeEpic(int id); //удалить эпик

    void removeSubTask(int id); //удалить сабтаск

    void clearTask(); // очистили список Таск

    void clearSubtasks();

    void clearEpics();

    Task getTask(int id); // получение по идентификатору

    Epic getEpic(int id);

    SubTask getSubtask(int id);

    void updateTask(Task task); //обновление Task

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    ArrayList<Task> getListSub(int id); //получение списка подзадач одного эпика

    Set<Task> getListOfPriority();

    List<Task> getAll();

    void setAllTasks();
}