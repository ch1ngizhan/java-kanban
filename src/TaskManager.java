
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counter ;

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;




    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        counter = 1;
    }
    private int generateId() {
        return counter++;
    }

    public ArrayList<Task> getListTask(){
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getListEpics(){
        return new ArrayList<>(epics.values());

        }

    public ArrayList<Subtask> getListSubtasks(){
        return new ArrayList<>(subtasks.values());
    }


    public void deleteAllTasks(){
        tasks.clear();
    }


    public void deleteAllEpics(){
        subtasks.clear();
        epics.clear();
    }


    public void deleteAllSubtasks(){
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtaskID();
            updateEpicStatus(epic.getId());
        }

        subtasks.clear();
    }


    public Epic getByIDEpics(int id){
        return epics.get(id);
    }

    public Task getByIDTask(int id){
        return tasks.get(id);
    }

    public Subtask getByIDSubtasks(int id){
        return subtasks.get(id);
    }


    public void createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }


    public void createSubtask(Subtask subtask){
        if (epics.containsKey(subtask.getEpicID())) {
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicID()).addSubtaskID(subtask.getId());
            updateEpicStatus(subtask.getEpicID());
        }

    }

    public  void createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(),epic);
    }

    public void updateTask (int id,Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask (int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            if (subtasks.get(id).getEpicID() == subtask.getEpicID()) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(subtask.getEpicID());
            }
        }
    }

    public void updateEpic(int id,String title,String description) {
        if (epics.containsKey(id)) {
            epics.get(id).setTitle(title);
            epics.get(id).setDescription(description);
        }
    }


    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            epics.get(subtask.getEpicID()).removeSubtask(id);
            subtasks.remove(id);
            updateEpicStatus(subtask.getEpicID());
        }


    }

    public void deleteEpic(int epicID) {
        Epic epic =epics.get(epicID);
        if(epic != null) {
            for (int id : epic.getSubtasksID()){
                subtasks.remove(id);
            }
            epics.remove(epicID);
        }


    }


    public ArrayList<Subtask> getSubtaskForEpic(int epicID){
        ArrayList<Subtask> result = new ArrayList<>();
        if(epics.containsKey(epicID)){
            for (int id : epics.get(epicID).getSubtasksID()){
                result.add(subtasks.get(id));
            }
        }
        return result;
    }


    private void updateEpicStatus(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<Subtask> subtaskForEpic = getSubtaskForEpic(epicID);
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtaskForEpic) {
            if (subtask.getStatus() != Status.NEW){
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE){
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

}

