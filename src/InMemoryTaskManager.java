
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int counter ;

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final ArrayList<Task> history ;



    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        history =new ArrayList<>(10);
        counter = 1;
    }
    private int generateId() {
        return counter++;
    }

    @Override
    public ArrayList<Task> getListTask(){
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getListEpics(){
        return new ArrayList<>(epics.values());

        }

    @Override
    public ArrayList<Subtask> getListSubtasks(){
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void deleteAllTasks(){
        tasks.clear();
    }


    @Override
    public void deleteAllEpics(){
        subtasks.clear();
        epics.clear();
    }


    @Override
    public void deleteAllSubtasks(){
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtaskID();
            updateEpicStatus(epic.getId());
        }

        subtasks.clear();
    }


    @Override
    public Epic getByIDEpics(int id){
        return epics.get(id);
    }

    @Override
    public Task getByIDTask(int id){
        return tasks.get(id);
    }

    @Override
    public Subtask getByIDSubtasks(int id){
        return subtasks.get(id);
    }


    @Override
    public void createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }


    @Override
    public void createSubtask(Subtask subtask){
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
        epics.put(epic.getId(),epic);
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
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            epics.get(subtask.getEpicID()).removeSubtask(id);
            subtasks.remove(id);
            updateEpicStatus(subtask.getEpicID());
        }


    }

    @Override
    public void deleteEpic(int epicID) {
        Epic epic =epics.get(epicID);
        if(epic != null) {
            for (int id : epic.getSubtasksID()){
                subtasks.remove(id);
            }
            epics.remove(epicID);
        }


    }


    @Override
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


    @Override
   public ArrayList<Task> getHistory (){
        return new ArrayList<Task>(history);
   }




}

