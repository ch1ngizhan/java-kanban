package model;

import manager.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;//описание
    protected Status status;
    protected int id;
    protected Type type;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status status, int id,
                LocalDateTime startTime, Duration duration) {
        this(title, description, status, startTime, duration);
        this.id = id;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "\nЗадача: " + title +
                "\nID:" + id +
                "\nСтатус: " + status +
                "\nОписание:" + description;


    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, id);
    }


    public String formatString() {
        return String.format("%s,%S,%s,%s,%s", id, type, title, status, description);
    }


}
