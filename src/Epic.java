import java.util.ArrayList;


public class Epic extends Task{
    ArrayList<Integer>subtasksID;

    public Epic(String title, String description,int id,Status status) {
        super(title, description,id,status);
        this.subtasksID = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    public void addSubtaskID(int subtaskID) {
        subtasksID.add(subtaskID);
    }


    public void removeSubtask(int subtaskID) {
        subtasksID.remove(subtaskID);
    }


    public void deleteAllSubtaskID(){
        subtasksID.clear();
    }

    @Override
    public String toString() {
        return "\nЭпик: " + getTitle()+
                "\nID:" + getId() +
                "\nКол-во подзадач :"+ subtasksID.size() +
                "\nСтатус: " + getStatus() +
                "\nОписание:" + getDescription() ;
    }
}
