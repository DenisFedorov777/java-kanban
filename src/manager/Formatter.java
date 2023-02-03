package manager;

import tasks.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Formatter {
    FileBackedTasksManager manager = new FileBackedTasksManager(new File("DataFile.csv"));

    public void writeToHistory(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case EPIC:
                manager.epics.put(id, (Epic) task);
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                manager.subTasks.put(id, subTask);
                manager.epics.get(subTask.getEpicId()).getSubtaskList().add(id);
                break;
            default:
                manager.tasks.put(id, task);
                break;
        }
    }

    public void addToHistory(int id) {
        if (manager.epics.containsKey(id)) {
            manager.historyManager.add(manager.epics.get(id));
        } else if (manager.subTasks.containsKey(id)) {
            manager.historyManager.add(manager.subTasks.get(id));
        } else if (manager.tasks.containsKey(id)) {
            manager.historyManager.add(manager.tasks.get(id));
        }
    }

    public String toString(Task task) {
        String idEpic = String.valueOf(task.getEpicId());
        if (idEpic == "null") {
            idEpic = "";
        }
        return String.format("%d;%S;%s;%s;%S;%s", task.getId(), task.getType(),
                task.getName(), task.getDescription(), task.getStatus(), idEpic);
    }

    public Task fromString(String value) { //черновик
        final String[] strings = value.split(";"); // разделитель
        final int id = Integer.parseInt(strings[0]); // id
        final TypesTask type = TypesTask.valueOf(strings[1]); //тип задачи
        final String name = strings[2]; //название
        final String description = strings[3]; //описание
        final Status status = Status.valueOf(strings[4]); //статус
        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                final int epicId = Integer.parseInt(strings[5]);
                return new SubTask(id, name, description, status, epicId);
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
