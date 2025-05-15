package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    HistoryManager history;
    private int counter;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );


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

    protected int getCounter() {
        return counter;
    }

    protected void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    protected void addTask(Task task) {
        tasks.put(task.getId(), task);
        counter++;
        prioritizedTasks.add(task);
    }


    protected void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicID()).addSubtaskID(subtask.getId());
            updateEpicStatus(subtask.getEpicID());
            prioritizedTasks.add(subtask);
        }
        counter++;

    }


    protected void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        counter++;
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
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }


    @Override
    public void deleteAllEpics() {
        for (Integer id : subtasks.keySet()) {
            history.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
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
            prioritizedTasks.remove(subtasks.get(id));
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
    public void createTask(Task task) {
        if (hasTimeIntersections(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }

        task.setId(generateId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }

    }


    @Override
    public void createSubtask(Subtask subtask) {
        if (hasTimeIntersections(subtask)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        if (epics.containsKey(subtask.getEpicID())) {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicID()).addSubtaskID(subtask.getId());
            updateEpicStatus(subtask.getEpicID());
        }


    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(int id, Task task) {
        if (hasTimeIntersections(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            prioritizedTasks.add(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        if (hasTimeIntersections(subtask)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        if (subtasks.containsKey(id)) {
            if (subtasks.get(id).getEpicID() == subtask.getEpicID()) {
                prioritizedTasks.remove(subtasks.get(id));
                prioritizedTasks.add(subtask);
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
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            history.remove(id);
        }

    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            epics.get(subtask.getEpicID()).removeSubtask(id);
            subtasks.remove(id);
            prioritizedTasks.remove(subtask);
            history.remove(id);
            updateEpicStatus(subtask.getEpicID());
        }


    }

    @Override
    public void deleteEpic(int epicID) {
        Epic epic = epics.get(epicID);
        if (epic != null) {
            for (int id : epic.getSubtasksID()) {
                prioritizedTasks.remove(subtasks.get(id));
                subtasks.remove(id);
                history.remove(id);
            }
            epics.remove(epicID);
            history.remove(epicID);
        }
    }


    @Override
    public List<Subtask> getSubtaskForEpic(int epicID) {
        return epics.containsKey(epicID) ?
                epics.get(epicID).getSubtasksID().stream()
                        .map(subtasks::get)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

    }


    private void updateEpicStatus(int epicID) {
        Epic epic = epics.get(epicID);
        List<Subtask> subtaskForEpic = getSubtaskForEpic(epicID);

        if (subtaskForEpic.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allNew = subtaskForEpic.stream()
                .allMatch(subtask -> subtask.getStatus() == Status.NEW);
        boolean allDone = subtaskForEpic.stream()
                .allMatch(subtask -> subtask.getStatus() == Status.DONE);
        ;

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        epic.updateTime(subtaskForEpic);

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

    private boolean isTasksIntersect(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }

    public boolean hasTimeIntersections(Task newTask) {
        if (newTask == null || newTask.getStartTime() == null){
            return false;
        }
        return prioritizedTasks.stream()
                .filter(task -> task.getId()!=newTask.getId())
                .filter(task -> !task.equals(newTask))
                .anyMatch(task -> isTasksIntersect(task, newTask));
    }


    public List<Task> getHistory() {
        return new ArrayList<>(history.getHistory());
    }
}

