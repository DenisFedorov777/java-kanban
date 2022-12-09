package Tasks;

import Status.StatusEnum;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int epicId) {//для создания задачи
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, StatusEnum status) {  //для обновления
        super(id, name, description, status);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}