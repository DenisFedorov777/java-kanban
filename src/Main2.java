import main.manager.FileBackedTasksManager;
import main.server.HttpTaskServer;
import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class Main2 {
    public static void main(String[] args) throws IOException {
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
        HttpTaskServer server = new HttpTaskServer(man);
    }
}
