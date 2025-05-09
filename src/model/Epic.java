package model;

import manager.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {
    private final ArrayList<Integer> subtasksID;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, Status.NEW,null,null);
        this.subtasksID = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Epic(String title, String description, Status status, int id) {
        super(title, description, status, id,null,null);
        this.subtasksID = new ArrayList<>();
        this.type = Type.EPIC;

    }


    public ArrayList<Integer> getSubtasksID() {
        return new ArrayList<>(subtasksID);
    }

    public void addSubtaskID(int subtaskID) {
        subtasksID.add(subtaskID);
    }


    public void removeSubtask(int subtaskID) {
        subtasksID.remove(Integer.valueOf(subtaskID));
    }


    public void deleteAllSubtaskID() {
        subtasksID.clear();
    }

    @Override
    public String toString() {
        return "\nЭпик: " + getTitle() +
                "\nID:" + getId() +
                "\nКол-во подзадач :" + subtasksID.size() +
                "\nСтатус: " + getStatus() +
                "\nОписание:" + getDescription();
    }
}
