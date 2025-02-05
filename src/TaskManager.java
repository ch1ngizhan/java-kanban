
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counter ;

    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;




    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        counter = 1;
    }
    public int generateId() {
        return counter++;
    }

    public void getListTask(){
        for (Integer id : tasks.keySet() ){
            System.out.println(tasks.get(id));
        }
    }

    public void getListEpics(){
        for (Integer id : epics.keySet() ){
                System.out.println(epics.get(id));
            }

        }

    public void getListSubtasks(){
        for (Integer id : subtasks.keySet() ){
            System.out.println(subtasks.get(id));
        }
    }


    public void deleteAllTasks(){
        tasks.clear();
    }


    public void deleteAllEpics(){
        epics.clear();
    }


    public void deleteAllSubtasks(){
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
        tasks.put(task.getId(), task);
    }


    public void createSubtask(Subtask subtask){
        subtasks.put(subtask.getId(),subtask);
        epics.get(subtask.getEpicID()).addSubtaskID(subtask.getId());

    }

    public  void createEpic(Epic epic){
        epics.put(epic.getId(),epic);
    }

    public void updateTask (Task task) {
        tasks.put(task.getId(), task);

    }

    public void updateSubtask (Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicID());

    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }


    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubtask(int id) {
        epics.get(subtasks.get(id).getEpicID()).removeSubtask(id);
        subtasks.remove(id);

    }

    public void deleteEpic(int id) {
        epics.get(id).deleteAllSubtaskID();
        epics.remove(id);
    }


    public ArrayList<Subtask> getSubtaskForEpic(int epicID){
        ArrayList<Subtask> result = new ArrayList<>();
        for (int id : epics.get(epicID).getSubtasksID()){
            result.add(subtasks.get(id));
        }
        return result;
    }


    public void updateEpicStatus(int epicID) {
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
        updateEpic(epic);

    }

}

