package main.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, long duration, LocalDateTime startTime, Status status) {
        super(name, description, status, duration, startTime);
    }

    public Epic(int id, String name, String description, long duration, LocalDateTime startTime, Status status) {
        super(id, name, description, duration, startTime, status);
    }

    public void setSubtaskList(ArrayList<Integer> subtasks) {
        this.subtaskList = subtasks;
    }

    public ArrayList<Integer> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public TypesTask getType() {
        return TypesTask.EPIC;
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