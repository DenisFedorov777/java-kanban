import main.manager.FileBackedTasksManager;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {

        File file = new File("resources/DataFile.csv");
        FileBackedTasksManager man = new FileBackedTasksManager(file);

        man.createEpic(new Epic("попить чаю", "как попить чай"));
        man.createSubTask(new SubTask("чай", "налить кипяток", 10,
                LocalDateTime.of(2023, Month.FEBRUARY, 8, 22, 1), 1));
        System.out.println(man.getEpic(1));
        man.createSubTask(new SubTask("чай", "заварить чай", 10,
                LocalDateTime.of(2023, Month.FEBRUARY, 7, 10, 15), 1));
        man.createSubTask(new SubTask("чай", "добавить сахар", 15,
                LocalDateTime.of(2023, Month.FEBRUARY, 6, 12, 00),1));

        man.createTask(new Task("одеться", "одеть штаны", 1,
                LocalDateTime.of(2023, Month.FEBRUARY, 5, 10, 10)));
        man.createTask(new Task("одеться", "надеть рубашку", 1,
                LocalDateTime.of(2023, Month.FEBRUARY, 4, 10, 10)));

        man.createEpic(new Epic("посмотреть кино", "найти пульт"));
        man.createSubTask(new SubTask("найти пульт", "в спальне", 8,
                LocalDateTime.of(2023, Month.FEBRUARY, 3, 22, 1), 1));

        man.getTask(7);
        System.out.println(man.getEpicList());
        man.getSubtask(2);
        System.out.println(man.getSubtask(2));

        man.getEpic(1);
        man.getEpicList();
        man.getHistory();
        man.removeSubTask(3);

        man.createEpic(new Epic("девятый эпик", "Описание первого эпика"));
        man.createSubTask(new SubTask("первый сабтаск", "Описание первого сабтаска", 9,
                LocalDateTime.of(2023, Month.FEBRUARY, 2, 10, 15), 9));

        man.createSubTask(new SubTask("Второй сабтаск", "Описание второго сабтаска", 10,
                LocalDateTime.of(2023, Month.FEBRUARY, 1, 10, 15), 9));
        man.createSubTask(new SubTask("Третий сабтаск", "Описание третьего сабтаска", 8,
                LocalDateTime.of(2023, Month.FEBRUARY, 1, 15, 15), 9));
        man.getEpic(1);
        man.createTask(new Task("попить чай", "просто попить чаю", 2,
                LocalDateTime.of(2023, Month.FEBRUARY, 1, 17, 10)));
        System.out.println(man.getTaskList());
        System.out.println("----------" + "таски" + "---------");
        man.getSubtask(2);
        man.updateSubTask(new SubTask(2, "чай", "налить кипяток", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 31, 22, 10), 10, 1));
        man.getSubtask(4);
        man.updateTask(new Task(5, "одеться", "надеть рубашку", 20,
                LocalDateTime.of(2023, Month.FEBRUARY, 5, 23, 30), Status.DONE));
        man.createEpic(new Epic("Пустой эпик", "Описание пустого эпика"));
        System.out.println(man.getEpic(1));
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);
        manager.getTaskList();
        manager.getEpicList();
        manager.getSubTaskList();
        manager.getHistory();
    }
}