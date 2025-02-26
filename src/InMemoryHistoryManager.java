import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history ;

    InMemoryHistoryManager() {
        history = new ArrayList<>();
    }
    @Override
    public ArrayList<Task> getHistory(){
        return new ArrayList<Task>(history);
    }


    @Override
    public void add(Task task){
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }
}
