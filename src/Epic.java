import java.util.HashMap;

public class Epic extends Task{
    HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description,HashMap<Integer, Subtask>subtasks) {
        super(title, description);
        this.subtasks = subtasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
