public class Subtask extends Task {
        private int epicID;

    public Subtask(String title, String description,int id,Status status,int epicID) {
        super(title, description,id,status);
        this.epicID = epicID;

    }


    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "\nПодзадача: " + getTitle()+
                "\nEpicID:"+ getEpicID()+
                "\nID:" + getId() +
                "\nСтатус: " + getStatus() +
                "\nОписание:" + getDescription() ;
    }
}
