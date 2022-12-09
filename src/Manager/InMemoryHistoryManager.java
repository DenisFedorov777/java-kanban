package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public static final Integer HISTORY_MAX_SIZE = 10;

    List<Task> historyList = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (historyList.size() < HISTORY_MAX_SIZE) {
            historyList.add(task);
        } else if (historyList.size() > HISTORY_MAX_SIZE) {
            historyList.remove(0);
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}