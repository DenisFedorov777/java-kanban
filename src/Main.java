import Manager.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("Задача 1", "Описание 1"));
        taskManager.createTask(new Task("Задача 2", "описание задачи 2"));

        taskManager.createEpic(new Epic("эпик1", " описание эпика 1"));

        taskManager.createSubTask(new SubTask("подзадача 1", "описание подзадачи 1 ", 3));
        taskManager.createSubTask(new SubTask("подзадача 2", "описание подзадачи 2 ", 3));

        taskManager.createEpic(new Epic("эпик2", "описание эпика2"));
        taskManager.createSubTask(new SubTask("подзадача 1", "описание подзадачи 1 ", 6));
        taskManager.createSubTask(new SubTask("подзадача 2", "описание подзадачи 2 ", 6));

        taskManager.updateTask(new Task(1,"Новый Таск", "Описание нового Таска", "IN_PROGRESS"));
        taskManager.updateSubTask(new SubTask(4, "Новая подзадача 1", "Описание нов.подзадачи", "DONE", 3));
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());

        taskManager.removeEpic(6);
        System.out.println(taskManager.getEpicList());

    }
}
