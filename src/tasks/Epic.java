package tasks;

import service.Status;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskList;

    public Epic(String name, String description) {// для создания
        super(name, description);
        this.subtaskList = new ArrayList<>();
    }

    public Epic(int id, String name, String description) { //для обновления
        super(name, description);
        this.setId(id);
    }

    public Epic(int id, String name, String description, Status status) { //для обновления
        super(name, description);
        this.setId(id);
    }

    public void setSubtaskList(ArrayList<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public ArrayList<Integer> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskList=" + subtaskList +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtaskList.equals(epic.subtaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }
}