package main.manager;

import main.tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Formatter {

    public static String toString(Task task) {
        String idEpic = String.valueOf(task.getEpicId());
        if (idEpic.equals("null")) {
            idEpic = "";
        }
        return String.format("%d;%S;%s;%s;%S;%s;%s;%s", task.getId(), task.getType(),
                task.getName(), task.getDescription(),task.getDuration(),task.getStartTime(), task.getStatus(), idEpic);
    }

    public static Task fromString(String value) { //черновик
        final String[] strings = value.split(";"); // разделитель
        final int id = Integer.parseInt(strings[0]); // id
        final TypesTask type = TypesTask.valueOf(strings[1]); //тип задачи
        final String name = strings[2]; //название
        final String description = strings[3]; //описание
        long duration = Long.parseLong(strings[4]); //статус
        LocalDateTime startTime;
        if(strings[5].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(strings[5]);
        }
        final Status status = Status.valueOf(strings[6]);
        switch (type) {
            case TASK:
                return new Task(id, name, description, duration, startTime, status);
            case EPIC:
                return new Epic(id, name, description, duration, startTime, status);
            case SUBTASK:
                final int epicId = Integer.parseInt(strings[7]);
                return new SubTask(id, name, description, status, startTime, duration, epicId);
            default:
                throw new IllegalArgumentException("Неправильный тип задачи");
        }
    }

    public static String historyToString(HistoryManager manager) { //сохр.историю в csv
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        if (history.isEmpty()) {
            return "";
        }
        for (Task task : history) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) { //восстановление истории из CSV
        List<Integer> fromCSV = new ArrayList<>();
        if (value != null) {
            String[] id = value.split(",");
            for (String num : id) {
                fromCSV.add(Integer.parseInt(num));
            }
            return fromCSV;
        }
        return fromCSV;
    }
}