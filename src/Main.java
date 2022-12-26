import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;

public class Main {
    public static void main(String[] args) {
        TaskManager man = Managers.getDefault();

        man.createEpic(new Epic("Первый эпик", "Описание первого эпика"));
        man.createSubTask(new SubTask("первый сабтаск", "Описание первого сабтаска", 1));
        man.createSubTask(new SubTask("Второй сабтаск", "Описание второго сабтаска", 1));
        man.createSubTask(new SubTask("Третий сабтаск", "Описание третьего сабтаска", 1));
        man.getEpic(1);
        man.getSubtask(2);
        man.getSubtask(4);
        man.createEpic(new Epic("Пустой эпик", "Описание пустого эпика"));
        man.getEpic(5);
        man.getEpic(1);
        System.out.println("--------------------------------");
        System.out.println("История");
        System.out.println(man.getHistory());
        System.out.println("--------------------------------");
        man.removeSubTask(4);
        man.removeEpic(5);
        System.out.println("--------------------------------");
        System.out.println(man.getHistory());
        System.out.println("--------------------------------");

    }
}