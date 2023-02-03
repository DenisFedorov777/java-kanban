import manager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {
       // TaskManager man = Managers.getDefault();

        File file = new File("resources/DataFile.csv");
        FileBackedTasksManager man = new FileBackedTasksManager(file);

        man.createEpic(new Epic("попить чаю", "как попить чай"));
        man.createSubTask(new SubTask("чай", "налить кипяток", 1));
        man.createSubTask(new SubTask("чай", "заварить чай", 1));
        man.createSubTask(new SubTask("чай", "добавить сахар", 1));

        man.createTask(new Task("одеться", "одеть штаны"));
        man.createTask(new Task("одеться", "надеть рубашку"));

        man.createEpic(new Epic("посмотреть кино", "найти пульт"));
        man.createSubTask(new SubTask("найти пульт", "в спальне", 8));

        man.getTask(7);
        System.out.println(man.getEpicList());
        man.getSubtask(2);
        System.out.println(man.getSubtask(2));

        man.getEpic(1);
        man.getEpicList();
        man.getHistory();
        man.removeSubTask(3);

        man.createEpic(new Epic("Первый эпик", "Описание первого эпика"));
        man.createSubTask(new SubTask("первый сабтаск", "Описание первого сабтаска", 1));
        man.createSubTask(new SubTask("Второй сабтаск", "Описание второго сабтаска", 1));
        man.createSubTask(new SubTask("Третий сабтаск", "Описание третьего сабтаска", 1));
        man.getEpic(1);
        man.createTask(new Task("попить чай", "просто попить чаю"));
        System.out.println(man.getTaskList());
        System.out.println("----------" + "таски" + "---------");
        man.getSubtask(2);
        man.updateSubTask(new SubTask(2, "чай", "налить кипяток", Status.DONE));
        man.getSubtask(4);
        man.updateTask(new Task(6, "одеться", "надеть рубашку", Status.DONE));
        man.createEpic(new Epic("Пустой эпик", "Описание пустого эпика"));
        System.out.println(man.getEpic(1));
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);
        manager.getTaskList();
        manager.getEpicList();
        manager.getSubTaskList();
        manager.getHistory();
    }
}