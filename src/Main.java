@SuppressWarnings("java:S106") // Подавляем предупреждение для всего класса
public class Main {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Лабораторная работа 6");
        System.out.println("Рефлексия, аннотации и работа с файловой системой");
        System.out.println("Студент: Симоненко Регина");
        System.out.println("Группа: Фит241");
        System.out.println("=========================================\n");

        // Задание 1: Рефлексия и аннотации
        executeTask1();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Задание 2: Работа с файловой системой
        executeTask2();

        System.out.println("\n=========================================");
        System.out.println("Лабораторная работа 6 завершена!");
        System.out.println("=========================================");
    }

    private static void executeTask1() {
        System.out.println("ЗАДАНИЕ 1: Рефлексия и аннотации");
        System.out.println("-----------------------------------------");

        Invoker invoker = new Invoker();
        invoker.invokeAnnotatedMethods();
    }

    private static void executeTask2() {
        System.out.println("ЗАДАНИЕ 2: Работа с файловой системой");
        System.out.println("-----------------------------------------");

        FileSystemManager fsManager = new FileSystemManager("Simonenko", "Regina");
        fsManager.performFileOperations();
    }
}