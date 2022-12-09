import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import static Status.StatusEnum.*;

public class Main {
    public static void main(String[] args) {
        // Тестирование Тасков
        TaskManager manag = Managers.getDefault();

        manag.createTask(new Task("Задача 1", "Описание 1"));
        manag.createTask(new Task("Задача 2", "описание задачи 2"));
        System.out.println(manag.getTaskList());
        manag.getTask(1);
        manag.getTask(2);
        System.out.println("История");
        System.out.println(manag.getHistory());
        manag.removeTask(1);
        manag.updateTask(new Task(2, "Задача 2", "Описание 2", DONE));
        System.out.println(manag.getTaskList());
        System.out.println(manag.getTask(2));
        manag.clearTask();
        System.out.println(manag.getTaskList());
        System.out.println();

        manag.getTask(1);
        manag.getEpicList();
        manag.getSubTaskList();


        manag.createEpic(new Epic("эпик1", " описание эпика 1"));
        manag.createSubTask(new SubTask("подзадача 1", "описание подзадачи 1 ", 3));
        manag.getListSub(3);
        System.out.println();

        manag.createEpic(new Epic("Сварить суп", "Варить долго"));
        manag.createSubTask(new SubTask("Почистить картошку", "Каротшку чистить кольцами ", 5));
        manag.createSubTask(new SubTask("Почистить лук", "Лук тоже нужен", 5));
        System.out.println(manag.getSubTaskList());
        System.out.println(manag.getEpicList());
        System.out.println();

        manag.updateSubTask(new SubTask(6, "Мясо", "Мясо заменит лук", DONE));
        manag.updateEpic(new Epic(3, "Сварить компот", "Для этого достать ягоды"));
        System.out.println(manag.getSubTaskList());
        System.out.println(manag.getEpicList());
        System.out.println();
        manag.removeSubTask(6);
        manag.removeEpic(3);

        System.out.println(manag.getSubTaskList());
        System.out.println(manag.getEpicList());
    }
}