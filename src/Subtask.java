public class Subtask extends Task {
        private int epicID;

    public Subtask(String title, String description,Status status ,int epicID) {
        super(title, description,status );
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
