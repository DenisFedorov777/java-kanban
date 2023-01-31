package tasks;

import service.Status;
import java.util.Objects;
import static service.Status.NEW;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description) {//для создания задач
        this.name = name;
        this.description = description;
        this.status = NEW;
    }

    public Task(int id, String name, String description, Status status) {//для обновления задач
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description)
                && status == task.status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }
}