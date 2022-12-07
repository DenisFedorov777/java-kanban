import Manager.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

//Здравствуйте! Спасибо за высокую оценку моей работы! Постарался отработать все замечания.
//Надеюсь успеть до жесткого дедлайна сдать еще 4 ТЗ.
public class Main {
    public static void main(String[] args) {
        // Тестирование Тасков
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Задача 1", "Описание 1"));
        taskManager.createTask(new Task("Задача 2", "описание задачи 2"));
        System.out.println(taskManager.getTaskList());
        taskManager.removeTask(1);
        taskManager.updateTask(new Task(2, "Задача 2", "Описание 2", "DONE"));
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getTask(2));
        taskManager.clearTask();
        System.out.println(taskManager.getTaskList());
        System.out.println();

        taskManager.createEpic(new Epic("эпик1", " описание эпика 1"));
        taskManager.createSubTask(new SubTask("подзадача 1", "описание подзадачи 1 ", 3));
        taskManager.getListSub(3);
        System.out.println();

        taskManager.createEpic(new Epic("Сварить суп", "Варить долго"));
        taskManager.createSubTask(new SubTask("Почистить картошку", "Каротшку чистить кольцами ", 5));
        taskManager.createSubTask(new SubTask("Почистить лук", "Лук тоже нужен", 5));
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println();

        taskManager.updateSubTask(new SubTask(6, "Мясо", "Мясо заменит лук", "DONE"));
        taskManager.updateEpic(new Epic(3, "Сварить компот", "Для этого достать ягоды", "DONE"));
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println();

        taskManager.removeSubTask(6);
        taskManager.removeEpic(3);

        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
    }
}