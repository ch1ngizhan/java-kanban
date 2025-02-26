package model;

import java.util.Objects;

public class Task {
    protected String title;
    protected String description;//описание
    protected Status status;
    protected int id ;

    public Task(String title, String description,Status status) {
        this.title = title;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, id);
    }
}
