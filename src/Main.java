import manager.Managers;
import manager.TaskManager;
import model.Status;
import model.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager mngr = Managers.getDefault();

        /*mngr.createTask(new model.Task("Выспаться","Лечь спать в 23:00",model.Status.NEW));

        System.out.println("Создана задача 1:" + mngr.getByIDTask(1));

        mngr.createTask(new model.Task("Выспаться","Лечь спать в 00:00",model.Status.NEW ));

        System.out.println("Создана задача 2:" + mngr.getByIDTask(2));

        mngr.createEpic(new model.Epic("Успеть лечь в 23:00","Будет тяжело, но ты справишься."));

        System.out.println(mngr.getByIDEpics(3));

        mngr.createSubtask(new model.Subtask("Зал", "Заглянуть в зал на часик.",
                model.Status.NEW,3));

        System.out.println(mngr.getByIDSubtasks(4));

        mngr.createSubtask(new model.Subtask("Домашнии дела", "Постараться решить 50% домашних дел.",
                model.Status.NEW,3));

        System.out.println(mngr.getByIDSubtasks(5));

        mngr.createEpic(new model.Epic("Успеть лечь в 00:00","Будет невероятно тяжело ,но ты справишься."));
        System.out.println(mngr.getByIDEpics(6));

        mngr.createSubtask(new model.Subtask("Осознание", "Кого ты обманываешь.",model.Status.NEW
                ,6));

        System.out.println(mngr.getByIDSubtasks(7));

        System.out.println("Список задач:");
        mngr.getListTask();
        System.out.println();

        System.out.println("Список эпиков:");
        mngr.getListEpics();
        System.out.println();

        System.out.println("Список подклассов:");
        mngr.getListSubtasks();
        System.out.println();

        mngr.updateTask(1,new model.Task("Выспаться","Лечь спать в 23:00",model.Status.DONE ));
        mngr.updateSubtask(4,new model.Subtask("Зал", "Заглянуть в зал на часик.",model.Status.DONE,3));
        mngr.updateSubtask(7,new model.Subtask("Осознание", "Кого ты обманываешь.",model.Status.DONE,6));


        System.out.println("Создана задача 1:" + mngr.getByIDTask(1));


        System.out.println(mngr.getByIDEpics(3));

        System.out.println(mngr.getByIDSubtasks(4));

        System.out.println(mngr.getByIDEpics(6));

        System.out.println(mngr.getByIDSubtasks(7));

        mngr.deleteTask(2);
        mngr.deleteEpic(6);

        System.out.println("Список задач:");
        System.out.println(mngr.getListTask());
        System.out.println();

        System.out.println("Список эпиков:");
        System.out.println(mngr.getListEpics());
        System.out.println();

        System.out.println("Список подклассов:");
        System.out.println(mngr.getListSubtasks());
        System.out.println();*/

       /* mngr.createEpic(new model.Epic("Успеть лечь в 23:00","Будет тяжело, но ты справишься."));
        mngr.createSubtask(new model.Subtask("Домашнии дела", "Постараться решить 50% домашних дел.",
                model.Status.NEW,1));
        mngr.createSubtask(new model.Subtask("Зал", "Заглянуть в зал на часик.",
                model.Status.DONE,1));
        mngr.createTask(new model.Task("Выспаться","Лечь спать в 00:00",model.Status.NEW ));
        mngr.printAllTasks();*/

       Task task = new Task("Выспаться","Лечь спать в 00:00", Status.NEW );
        task.setId(2);
        mngr.createTask(task);
        System.out.println(task.getId());
                mngr.getByIDTask(1);

        System.out.println(task.getId());





    }
}
