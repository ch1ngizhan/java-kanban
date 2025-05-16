package model;

import manager.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Epic extends Task {
    private final ArrayList<Integer> subtasksID;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, Status.NEW, null, null);
        this.subtasksID = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Epic(String title, String description, Status status, int id) {
        super(title, description, status, id, null, null);
        this.subtasksID = new ArrayList<>();
        this.type = Type.EPIC;

    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private Duration getDuration(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            return Duration.ZERO;
        }
        return Duration.ofMinutes(
                subtasks.stream()
                        .filter(subtask -> subtask.getDuration() != null)
                        .mapToLong(subtask -> subtask.getDuration().toMinutes())
                        .sum()
        );
    }

    private LocalDateTime getStartTime(List<Subtask> subtasks) {
        if (subtasksID.isEmpty()) {
            return null;
        }
        return subtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .min(Comparator.comparing(Subtask::getStartTime))
                .map(Subtask::getStartTime)
                .orElse(null);
    }

    public void updateTime(List<Subtask> subtasks) {
        this.startTime = getStartTime(subtasks);
        this.duration = getDuration(subtasks);

        this.endTime = (startTime != null && duration != null)
                ? startTime.plus(duration)
                : null;
    }


    public ArrayList<Integer> getSubtasksID() {
        return new ArrayList<>(subtasksID);
    }

    public void addSubtaskID(int subtaskID) {
        subtasksID.add(Integer.valueOf(subtaskID));
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
                "\nОписание:" + getDescription() +
                "\nНачало: " + startTime.toString() +
                "\nПродолжительность: " + duration.toMinutes() + " минут" +
                "\nОкончание: " + endTime.toString();
    }
}
