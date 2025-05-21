package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getListTask();

    List<Epic> getListEpics();

    List<Subtask> getListSubtasks();

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

    List<Subtask> getSubtaskForEpic(int epicID);

    void printAllTasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();


    boolean hasTimeIntersections(Task t);
}
