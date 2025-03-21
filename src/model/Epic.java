package model;

import manager.Type;

import java.util.ArrayList;


public class Epic extends Task {
    private final ArrayList<Integer> subtasksID;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtasksID = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Epic(String title, String description, Status status, int id) {
        super(title, description, status, id);
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
