import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getListTask();

    ArrayList<Epic> getListEpics();

    ArrayList<Subtask> getListSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Epic getByIDEpics(int id);

    Task getByIDTask(int id);

    Subtask getByIDSubtasks(int id);

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void updateTask(int id, Task task);

    void updateSubtask(int id, Subtask subtask);

    void updateEpic(int id, String title, String description);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int epicID);

    ArrayList<Subtask> getSubtaskForEpic(int epicID);

    void printAllTasks();
}
