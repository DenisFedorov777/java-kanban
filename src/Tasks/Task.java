package Tasks;

import Status.StatusEnum;

import java.util.Objects;

import static Status.StatusEnum.NEW;

public class Task {
    private int id;
    private String name;
    private String description;
    private StatusEnum status;

    public Task(String name, String description) {//для создания задач
        this.name = name;
        this.description = description;
        this.status = NEW;
    }

    public Task(int id, String name, String description, StatusEnum status) {//для обновления задач
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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