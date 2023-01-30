package manager;

import service.ManagerSaveException;
import service.Status;
import service.TypesTask;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager dataTasks = new FileBackedTasksManager(file);
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            int maxId = 0;
                while (br.ready()) {
                    Task task = dataTasks.fromString(line);
                    dataTasks.writeToHistory(task);
                    br.close();
                }
                String oneLine = br.readLine();
                for(int id: historyFromString(oneLine)) {
                    dataTasks.addToHistory(id);
                }
                dataTasks.id = maxId;
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка при чтении из данных из файла");
        }
        return dataTasks;
    }

    private void writeToHistory(Task task) {
        final int id = task.getId();
        switch (getType(task)){
            case EPIC:
                epics.put(id, (Epic) task);
            case SUBTASK:
                subTasks.put(id, (SubTask) task);
                epics.get(task.getId()).getSubtaskList().add(id);
            default:
                tasks.put(id, task);
                break;
        }
    }

    private void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        } else if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
    }

    private TypesTask getType(Task task) {
        if (task instanceof Epic) {
            return TypesTask.EPIC;
        } else if (task instanceof SubTask) {
            return TypesTask.SUBTASK;
        }
        return TypesTask.SIMPLE_TASK;
    }

    private String getSubtaskByEpicId(Task task) { //достать id эпика у сабтаска
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicId());
        }
        return "";
    }

    private String toString(Task task) {
        return String.format("%d,%S,%s,%s,%S,%s", task.getId(), getType(task),
                task.getName(), task.getDescription(), task.getStatus(), getSubtaskByEpicId(task));
    }

    private Task fromString(String value) { //черновик
        final String[] strings = value.split(";"); // разделитель
        final int id = Integer.parseInt(strings[0]); // id
        final TypesTask type = TypesTask.valueOf(strings[1].toUpperCase()); //тип задачи
        final String name = strings[2]; //название
        final String description = strings[3]; //описание
        final Status status = Status.valueOf(strings[4].toUpperCase()); //статус
        switch (type) {
            case SIMPLE_TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                final int epicId = Integer.parseInt(strings[5]);// или Integer.valueOf(split[5])?
                return new SubTask(id, name, description, status, epicId);
            default:
                throw new IllegalArgumentException("Неправильный тип задачи");
        }
    }

    private static String historyToString(HistoryManager manager) { //сохр.историю в csv
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

    private static List<Integer> historyFromString(String value) { //восстановление истории из CSV
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

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = super.getSubtask(id);
        save();
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epics.get(id);
    }

    @Override
    public Task getTask(int id) { // получение по идентификатору
        Task task = super.getTask(id);
        save();
        return tasks.get(id);
    }

    public void save() {
        System.out.println("Сохранили");
    }

    @Override
    public void createTask(Task task) { //создали Таск
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) { //создали эпик
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) { //создали сабтаск
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void removeTask(int id) { //удалили по id
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) { //удалить эпик
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubTask(int id) { //удалить сабтаск
        super.removeSubTask(id);
        save();
    }

    @Override
    public void clearTask() { // очистили список Таск
        super.clearTask();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpics() { //удалить все эпики
        super.clearEpics();
        save();
    }

    @Override
    public void updateTask(Task task) { //обновление Task
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }
}