public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager mngr = new TaskManager();

        /*mngr.createTask(new Task("Выспаться","Лечь спать в 23:00",Status.NEW));

        System.out.println("Создана задача 1:" + mngr.getByIDTask(1));

        mngr.createTask(new Task("Выспаться","Лечь спать в 00:00",Status.NEW ));

        System.out.println("Создана задача 2:" + mngr.getByIDTask(2));

        mngr.createEpic(new Epic("Успеть лечь в 23:00","Будет тяжело, но ты справишься."));

        System.out.println(mngr.getByIDEpics(3));

        mngr.createSubtask(new Subtask("Зал", "Заглянуть в зал на часик.",
                Status.NEW,3));

        System.out.println(mngr.getByIDSubtasks(4));

        mngr.createSubtask(new Subtask("Домашнии дела", "Постараться решить 50% домашних дел.",
                Status.NEW,3));

        System.out.println(mngr.getByIDSubtasks(5));

        mngr.createEpic(new Epic("Успеть лечь в 00:00","Будет невероятно тяжело ,но ты справишься."));
        System.out.println(mngr.getByIDEpics(6));

        mngr.createSubtask(new Subtask("Осознание", "Кого ты обманываешь.",Status.NEW
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

        mngr.updateTask(1,new Task("Выспаться","Лечь спать в 23:00",Status.DONE ));
        mngr.updateSubtask(4,new Subtask("Зал", "Заглянуть в зал на часик.",Status.DONE,3));
        mngr.updateSubtask(7,new Subtask("Осознание", "Кого ты обманываешь.",Status.DONE,6));


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

        mngr.createEpic(new Epic("Успеть лечь в 23:00","Будет тяжело, но ты справишься."));
        mngr.createSubtask(new Subtask("Домашнии дела", "Постараться решить 50% домашних дел.",
                Status.NEW,1));
        mngr.createSubtask(new Subtask("Зал", "Заглянуть в зал на часик.",
                Status.DONE,1));
        System.out.println(mngr.getListEpics());
        mngr.deleteAllSubtasks();
        System.out.println(mngr.getListEpics());


    }
}
