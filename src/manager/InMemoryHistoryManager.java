package manager;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public static final Integer HISTORY_MAX_SIZE = 10;

    private LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        historyList.addLast(task);
        if (historyList.size() > HISTORY_MAX_SIZE) {
            historyList.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}