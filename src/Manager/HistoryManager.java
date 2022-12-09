package Manager;

import Tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}