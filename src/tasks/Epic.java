package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {// для создания
        super(name, description);

    }

    public Epic(int id, String name, String description) { //для обновления
        super(name, description);
        this.setId(id);
    }

    public Epic(int id, String name, String description, Status status) { //для обновления
        super(name, description);
        this.setId(id);
        this.setStatus(status);
    }

    public void setSubtaskList(ArrayList<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public ArrayList<Integer> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public TypesTask getType() {
        return TypesTask.EPIC;
    }

    public Integer getEpicId() {
        return null;
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