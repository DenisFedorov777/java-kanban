package main.manager;

import main.service.ManagerSaveException;
import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String HEAD = "id,type,name,description,duration,startTime,status,epicId";
    private static final String NEW_LINE = "\n";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager dataTasks = new FileBackedTasksManager(file);
        if (file.length() != 0) {
            try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                String line = br.readLine();
                int maxId = 0;
                if (line != null) {
                    while (br.ready()) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            Task task = Formatter.fromString(line);
                            dataTasks.writeToMaps(task);
                        } else {
                            break;
                        }
                    }
                    String oneLine = br.readLine();
                    if (oneLine != null) {
                        for (int id : Formatter.historyFromString(oneLine)) {
                            dataTasks.addToHistory(id);
                        }
                    }
                    dataTasks.id = maxId;
                }
            } catch (IOException exp) {
                throw new ManagerSaveException("Ошибка при чтении данных из файла.", exp);
            }
        }
        return dataTasks;
    }

    private void save() {
        try(FileWriter writer = new FileWriter(file, false)) {
            writer.write(HEAD + NEW_LINE);
            for (Task task : getTaskList()) {
                writer.write(Formatter.toString(task) + NEW_LINE);
            }
            for (Epic epic : getEpicList()) {
                writer.write(Formatter.toString(epic) + NEW_LINE);
            }
            for (SubTask subTask : getSubTaskList()) {
                writer.write(Formatter.toString(subTask) + NEW_LINE);
                writer.write("");
            }
            writer.write(NEW_LINE);
            writer.write(Formatter.historyToString(historyManager));
            } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    private void writeToMaps(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case EPIC:
                epics.put(id, (Epic) task);
                listOfPriority.add(task);
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                subTasks.put(id, subTask);
                epics.get(subTask.getEpicId()).getSubtaskList().add(id);
                listOfPriority.add(subTask);
                break;
            default:
                tasks.put(id, task);
                listOfPriority.add(task);
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

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = super.getSubtask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
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