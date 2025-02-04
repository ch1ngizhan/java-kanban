import java.util.HashMap;

public class TaskManager {
    private int counter = 0;

    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;


    public TaskManager() {
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


    public void clearTask(){
        tasks.clear();
    }


    public void clearEpics(){
        epics.clear();
    }


    public void clearSubtasks(){
        subtasks.clear();
    }


    public void getByIDEpics(int id){
        System.out.println(epics.get(id));
    }

    public void getByIDTask(int id){
        System.out.println(tasks.get(id));
    }

    public void getByIDSubtasks(int id){
        System.out.println(subtasks.get(id));
    }


    public void сreatTask(Task task){
        counter++;
       int id = task.getId() + counter;
        tasks.put(id,task);
    }

}

