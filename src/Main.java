import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import static Status.Status.*;
// Здравствуйте! Постарался исправить все замечания, спасибо за подсказки)
public class Main {
    public static void main(String[] args) {
        // Тестирование Тасков
        TaskManager man = Managers.getDefault();

        man.createTask(new Task("Задача 1", "Описание 1"));
        man.createTask(new Task("Задача 2", "описание задачи 2"));
        System.out.println(man.getTaskList());
        man.getTask(1);
        man.getTask(2);
        man.clearTask();
        System.out.println(man.getTaskList());
        System.out.println("История");
        System.out.println(man.getHistory());
        man.removeTask(1);
        man.updateTask(new Task(2, "Задача 2", "Описание 2", DONE));
        man.clearTask();
        System.out.println(man.getTaskList());
        System.out.println();

        man.getTask(1);
        man.getEpicList();
        man.getSubTaskList();

        man.createEpic(new Epic("эпик1", " описание эпика 1"));
        man.createSubTask(new SubTask("подзадача 1", "описание подзадачи 1 ", 3));
        man.getListSub(3);
        System.out.println();

        man.createEpic(new Epic("Сварить суп", "Варить долго"));
        man.createSubTask(new SubTask("Почистить картошку", "Каротшку чистить кольцами ", 5));
        man.createSubTask(new SubTask("Почистить лук", "Лук тоже нужен", 5));
        System.out.println(man.getSubTaskList());
        System.out.println(man.getEpicList());
        System.out.println();

        man.updateSubTask(new SubTask(6, "Мясо", "Мясо заменит лук", DONE));
        man.updateEpic(new Epic(3, "Сварить компот", "Для этого достать ягоды"));
        System.out.println(man.getSubTaskList());
        System.out.println(man.getEpicList());
        System.out.println();
        man.removeSubTask(6);
        man.removeEpic(3);

        System.out.println(man.getSubTaskList());
        System.out.println(man.getEpicList());
    }
}