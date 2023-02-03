package manager;

import service.ManagerSaveException;
import tasks.Status;
import tasks.TypesTask;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String HEAD = "id,type,name,description,status,epicId";
    private static final String NEW_LINE = "\n";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager dataTasks = new FileBackedTasksManager(file);
        try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line = br.readLine();
            int maxId = 0;
            while (br.ready()) {
                line = br.readLine();
                if(!line.isEmpty()) {
                    Task task = dataTasks.fromString(line);
                    dataTasks.writeToHistory(task);
                } else {
                    break;
                }
            }
            String oneLine = br.readLine();
            for (int id : historyFromString(oneLine)) {
                dataTasks.addToHistory(id);
            }
            dataTasks.id = maxId;
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка при чтении данных из файла.", exp);
        }
        return dataTasks;
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(HEAD + NEW_LINE);
            for (Task task : getTaskList()) {
                writer.write(toString(task) + NEW_LINE);
            }
            for (Epic epic : getEpicList()) {
                writer.write(toString(epic) + NEW_LINE);
            }
            for (SubTask subTask : getSubTaskList()) {
                writer.write(toString(subTask) + NEW_LINE);
                writer.write("");
            }
            writer.write(NEW_LINE);
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла");
        }
    }

    private void writeToHistory(Task task) {
        final int id = task.getId();
        switch (getType(task)) {
            case EPIC:
                epics.put(id, (Epic) task);
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                subTasks.put(id, subTask);
                epics.get(subTask.getEpicId()).getSubtaskList().add(id);
                break;
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
        return TypesTask.TASK;
    }

    private String getSubtaskByEpicId(Task task) { //достать id эпика у сабтаска
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicId());
        }
        return "";
    }

    private String toString(Task task) {
        return String.format("%d;%S;%s;%s;%S;%s", task.getId(), getType(task),
                task.getName(), task.getDescription(), task.getStatus(), getSubtaskByEpicId(task));
    }

    private Task fromString(String value) { //черновик
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
        save();
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        save();
        return epics.get(id);
    }

    @Override
    public Task getTask(int id) { // получение по id
        save();
        return tasks.get(id);
    }

    @Override
    public void createTask(Task task) {  //создали Таск
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