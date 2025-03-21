package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    HistoryManager history;
    private int counter;


    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        history = Managers.getDefaultHistory();
        counter = 1;
    }

    private int generateId() {
        return counter++;
    }

    @Override
    public ArrayList<Task> getListTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getListEpics() {
        return new ArrayList<>(epics.values());

    }

    @Override
    public ArrayList<Subtask> getListSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            history.remove(id);
        }
        tasks.clear();
    }


    @Override
    public void deleteAllEpics() {
        for (Integer id : subtasks.keySet()) {
            history.remove(id);
        }
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            history.remove(id);
        }
        epics.clear();
    }


    @Override
    public void deleteAllSubtasks() {
        for (Integer id : subtasks.keySet()) {
            history.remove(id);
        }
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtaskID();
            updateEpicStatus(epic.getId());
        }

        subtasks.clear();
    }


    @Override
    public Epic getByIDEpics(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            history.add(epic);
        }
        return epics.get(id);
    }

    @Override
    public Task getByIDTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            history.add(task);
        }
        return tasks.get(id);
    }

    @Override
    public Subtask getByIDSubtasks(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            history.add(subtask);
        }
        return subtasks.get(id);
    }


    @Override
    public void createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);

    }


    @Override
    public void createSubtask(Subtask  subtask) {
        if (epics.containsKey(subtask.getEpicID())) {
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicID()).addSubtaskID(subtask.getId());
            updateEpicStatus(subtask.getEpicID());
        }

    }

    @Override
    public void createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            if (subtasks.get(id).getEpicID() == subtask.getEpicID()) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(subtask.getEpicID());
            }
        }
    }

    @Override
    public void updateEpic(int id, String title, String description) {
        if (epics.containsKey(id)) {
            epics.get(id).setTitle(title);
            epics.get(id).setDescription(description);
        }
    }


    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            epics.get(subtask.getEpicID()).removeSubtask(id);
            subtasks.remove(id);
            history.remove(id);
            updateEpicStatus(subtask.getEpicID());
        }


    }

    @Override
    public void deleteEpic(int epicID) {
        Epic epic = epics.get(epicID);
        if (epic != null) {
            for (int id : epic.getSubtasksID()) {
                subtasks.remove(id);
                history.remove(id);
            }
            epics.remove(epicID);
            history.remove(epicID);
        }
    }


    @Override
    public List<Subtask> getSubtaskForEpic(int epicID) {
        List<Subtask> result = new ArrayList<>();
        if (epics.containsKey(epicID)) {
            for (int id : epics.get(epicID).getSubtasksID()) {
                result.add(subtasks.get(id));
            }
        }
        return result;
    }


    private void updateEpicStatus(int epicID) {
        Epic epic = epics.get(epicID);
        List<Subtask> subtaskForEpic = getSubtaskForEpic(epicID);
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtaskForEpic) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

    @Override
    public void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : getListTask()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : getListEpics()) {
            System.out.println(epic);

            for (Integer id : epic.getSubtasksID()) {
                System.out.println("--> " + getByIDSubtasks(id));
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : getListTask()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : history.getHistory()) {
            System.out.println(task);
        }
    }


    public List<Task> getHistory() {
        return new ArrayList<>(history.getHistory());
    }
}

