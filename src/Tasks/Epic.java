package Tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskList;


    public void setSubtaskList(ArrayList<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public ArrayList<Integer> getSubtaskList() {
        return subtaskList;
    }

    public Epic(String name, String description) {// для создания
        super(name, description);
        this.subtaskList = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {//для обновления эпика
        super(id, name, description);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskList=" + subtaskList +
                "} " + super.toString();
    }
}