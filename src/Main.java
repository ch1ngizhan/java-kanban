import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создаем две задачи
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Создаем эпик с тремя подзадачами
        Epic epic1 = new Epic("Epic 1", "Description Epic 1");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description Subtask 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description Subtask 2", Status.IN_PROGRESS, epic1.getId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description Subtask 3", Status.DONE, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        // Создаем эпик без подзадач
        Epic epic2 = new Epic("Epic 2", "Description Epic 2");
        taskManager.createEpic(epic2);

        // Запрашиваем задачи несколько раз в разном порядке
        taskManager.getByIDTask(task1.getId());
        taskManager.getByIDEpics(epic1.getId());
        taskManager.getByIDSubtasks(subtask1.getId());
        taskManager.getByIDTask(task2.getId());
        taskManager.getByIDEpics(epic2.getId());
        taskManager.getByIDSubtasks(subtask2.getId());
        taskManager.getByIDTask(task1.getId()); // Повторный запрос

        // Выводим историю после запросов
        System.out.println("История после запросов:");
        printHistory(taskManager.getHistory());

        // Удаляем задачу, которая есть в истории
        taskManager.deleteTask(task1.getId());

        // Выводим историю после удаления задачи
        System.out.println("\nИстория после удаления задачи:");
        printHistory(taskManager.getHistory());

        // Удаляем эпик с тремя подзадачами
        taskManager.deleteEpic(epic1.getId());

        // Выводим историю после удаления эпика
        System.out.println("\nИстория после удаления эпика:");
        printHistory(taskManager.getHistory());
    }

    // Вспомогательный метод для вывода истории
    private static void printHistory(List<Task> history) {
        if (history.isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : history) {
                System.out.println(task);
            }
        }
    }
}