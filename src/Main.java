import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import static service.Status.*;

public class Main {
    public static void main(String[] args) {
        TaskManager man = Managers.getDefault();

        man.createEpic(new Epic("Первый эпик", "Описание первого эпика"));
        man.createSubTask(new SubTask("первый сабтаск", "Описание первого сабтаска", 1));
        man.createSubTask(new SubTask("Второй сабтаск", "Описание второго сабтаска", 1));
        man.createSubTask(new SubTask("Третий сабтаск", "Описание третьего сабтаска", 1));
        System.out.println(man.getListSub(1));
        System.out.println(man.getEpicList());
        System.out.println(man.getSubTaskList());
        man.getEpic(1);
        man.getSubtask(2);
        man.getSubtask(4);
        man.createEpic(new Epic("Пустой эпик", "Описание пустого эпика"));
        System.out.println(man.getEpic(5));
        System.out.println(man.getEpic(1));
        System.out.println("--------------------------------");
        System.out.println(man.getHistory());
        System.out.println("--------------------------------");
        man.updateSubTask(new SubTask(3,"Второй сабтаск", "Описание второго сабтаска", IN_PROGRESS));
        System.out.println(man.getSubTaskList());
        man.removeSubTask(4);
        System.out.println(man.getSubTaskList());
        man.removeEpic(5);
        System.out.println("--------------------------------");
        System.out.println(man.getHistory());
        System.out.println("--------------------------------");
        System.out.println(man.getEpicList());
    }
}