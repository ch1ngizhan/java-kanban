package model;

import manager.Type;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String title, String description, Status status, int epicID, LocalDateTime startTime,
                   Duration duration) {
        super(title, description, status, startTime, duration);
        this.epicID = epicID;
        this.type = Type.SUBTASK;
    }

    public Subtask(String title, String description, Status status, int id, int epicID, LocalDateTime startTime,
                   Duration duration) {
        super(title, description, status, id, startTime, duration);
        this.epicID = epicID;
        this.type = Type.SUBTASK;
    }


    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "\nПодзадача: " + getTitle() +
                "\nEpicID:" + getEpicID() +
                "\nID:" + getId() +
                "\nСтатус: " + getStatus() +
                "\nОписание:" + getDescription() +
                "\nНачало: " + startTime.toString() +
                "\nПродолжительность: " + duration.toMinutes() + " минут" +
                "\nОкончание: " + getEndTime().toString();
    }

}
