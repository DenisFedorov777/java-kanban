package Tasks;

public class SubTask extends Task {

    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public SubTask(String name, String description, int epicId) {//для создания задачи
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, String status, int epicId) { //для обновления
        super(id, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }
}



