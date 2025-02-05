public class Task {
    private String title;
    private String description;//описание
    private Status status;
    private int id ;

    public Task(String title, String description,int id,Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    @Override
    public String toString() {
        return "\nЗадача: " + title+
                "\nID:" + id +
                "\nСтатус: " + status +
                "\nОписание:" + description ;


    }
}
