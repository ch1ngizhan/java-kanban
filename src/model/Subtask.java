package model;

import manager.Type;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
        this.type = Type.SUBTASK;
    }

    public Subtask(String title, String description, Status status, int id, int epicID) {
        super(title, description, status, id);
        this.epicID = epicID;
        this.type = Type.SUBTASK;
    }


    public int getEpicID() {
        return epicID;
    }
    public void setEpicID(int epicID){
        this.epicID =epicID;
    }

    @Override
    public String toString() {
        return "\nПодзадача: " + getTitle() +
                "\nEpicID:" + getEpicID() +
                "\nID:" + getId() +
                "\nСтатус: " + getStatus() +
                "\nОписание:" + getDescription();
    }
    @Override
    public String formatString (){
        return String.format("%s,%S,%s,%s,%s,%s",id,type,title,status,description,epicID);
    }
}
