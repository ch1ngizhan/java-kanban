package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath());
            String n = System.lineSeparator();
            String[] lines = content.split(n);
            int coun = 0;
            for (int i = 1; i < lines.length; i++) {
                String value = lines[i].trim();
                if (value.isEmpty()) {
                    continue;
                }
                Task task = fromString(value);

                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else {
                    manager.addTask(task);
                }
                if (coun < task.getId()) {
                    coun = task.getId();
                }
            }
            manager.setCounter(coun + 1);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0].trim());
        Type type = Type.valueOf(parts[1].trim());
        String name = parts[2].trim();
        Status status = Status.valueOf(parts[3].trim());
        String description = parts[4].trim();
        switch (type) {
            case TASK:
                return new Task(name, description, status, id);
            case EPIC:
                return new Epic(name, description, status, id);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(name, description, status, id, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи");
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("tasks.csv");

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("Задачи после загрузки:");
        for (Task task : loadedManager.getListTask()) {
            System.out.println(task);
        }
        for (Epic epic : loadedManager.getListEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : loadedManager.getListSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println(loadedManager.getCounter());

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    protected void addTask(Task task) {
        super.addTask(task);
    }

    @Override
    protected void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
    }

    @Override
    protected void addEpic(Epic epic) {
        super.addEpic(epic);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    @Override
    public void updateEpic(int id, String title, String description) {
        super.updateEpic(id, title, description);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int epicID) {
        super.deleteEpic(epicID);
        save();
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        lines.add("id,type,name,status,description,epic");
        for (Task task : getListTask()) {
            lines.add(task.formatString());
        }
        for (Epic epic : getListEpics()) {
            lines.add(epic.formatString());
        }
        for (Subtask subtask : getListSubtasks()) {
            lines.add(subtask.formatString());
        }
        try {
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }


}



