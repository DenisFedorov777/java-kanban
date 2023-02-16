package main.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private long duration;

    private LocalDateTime endTime;

    //конструктор для записи
    public Task(String name, String description, Status status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, long duration, LocalDateTime startTime) { //создание
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    //конструктор для обновления
    public Task(int id, String name, String description, long duration,
                LocalDateTime startTime, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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

    public TypesTask getType() {
        return TypesTask.TASK;
    }

    public Integer getEpicId() {
        return null;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        try {
            return this.startTime.plusMinutes(duration);
        } catch (NullPointerException exp) {
            System.out.println(exp.getMessage());
        }
        return null;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && name.equals(task.name)
                && description.equals(task.description) && status == task.status
                && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", status=" + status +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }
}