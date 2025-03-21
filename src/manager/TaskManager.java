package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {
    List<Task> getListTask();

    List<Epic> getListEpics();

    List<Subtask> getListSubtasks();

    void deleteAllTasks() throws IOException;

    void deleteAllEpics();

    void deleteAllSubtasks();

    Epic getByIDEpics(int id);

    Task getByIDTask(int id);

    Subtask getByIDSubtasks(int id);

    void createTask(Task task) throws IOException;

    void createSubtask(Subtask subtask) throws IOException;

    void createEpic(Epic epic) throws IOException;

    void updateTask(int id, Task task);

    void updateSubtask(int id, Subtask subtask);

    void updateEpic(int id, String title, String description);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int epicID);

    List<Subtask> getSubtaskForEpic(int epicID);

    void printAllTasks();

    List<Task> getHistory();
}
