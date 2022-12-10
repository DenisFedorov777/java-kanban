package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public static final Integer HISTORY_MAX_SIZE = 10;

    private LinkedList<Task> historyList = new LinkedList<>(); //??если написать <T extends Task>

    @Override
    public void add(Task task) {
        if (historyList.size() < HISTORY_MAX_SIZE) {
            historyList.addLast(task);
        } else {
            historyList.removeFirst();
            historyList.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}