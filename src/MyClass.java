public class MyClass {

    // Публичные методы
    public void publicMethod1() {
        System.out.println("Вызван публичный метод 1");
    }

    public String publicMethod2(String name) {
        String result = "Привет, " + name + "! (публичный метод 2)";
        System.out.println(result);
        return result;
    }

    // Защищенные методы
    @Repeat(times = 2)
    protected void protectedMethod1() {
        System.out.println("Вызван защищенный метод 1");
    }

    protected int protectedMethod2(int a, int b) {
        int sum = a + b;
        System.out.println("Защищенный метод 2: " + a + " + " + b + " = " + sum);
        return sum;
    }

    // Приватные методы
    @Repeat(times = 3)
    private void privateMethod1() {
        System.out.println("Вызван приватный метод 1");
    }

    @Repeat(times = 2)
    private String privateMethod2(String text, int count) {
        String result = "Приватный метод 2: " + text.repeat(count);
        System.out.println(result);
        return result;
    }

    private double privateMethod3(double x, double y) {
        double product = x * y;
        System.out.println("Приватный метод 3: " + x + " * " + y + " = " + product);
        return product;
    }

    // Еще один публичный метод без аннотации
    public void publicMethodWithoutAnnotation() {
        System.out.println("Публичный метод без аннотации");
    }

    // Новый метод с переменными параметрами
    public void demonstrateAllMethods(String userName, int number1, int number2, String testText, int repeatCount, double decimal1, double decimal2) {
        System.out.println("=== Демонстрация всех методов класса ===\n");

        // Используем публичные методы
        publicMethod1();
        String greeting = publicMethod2(userName);
        publicMethodWithoutAnnotation();

        // Используем защищенные методы
        protectedMethod1();
        int sumResult = protectedMethod2(number1, number2);

        // Используем приватные методы
        privateMethod1();
        String repeatedText = privateMethod2(testText, repeatCount);
        double productResult = privateMethod3(decimal1, decimal2);

        System.out.println("\n=== Результаты выполнения методов ===");
        System.out.println("Результат publicMethod2: " + greeting);
        System.out.println("Результат protectedMethod2: " + sumResult);
        System.out.println("Результат privateMethod2: " + repeatedText);
        System.out.println("Результат privateMethod3: " + productResult);
    }

    // Перегруженный метод для удобства
    public void demonstrateAllMethods() {
        demonstrateAllMethods("Иван", 5, 3, "Тест ", 2, 2.5, 4.0);
    }

    // Можно также добавить main метод для тестирования
    public static void main(String[] args) {
        MyClass myObject = new MyClass();

        if (args.length >= 6) {
            // Использовать аргументы командной строки
            myObject.demonstrateAllMethods(
                    args[0],
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    args[3],
                    Integer.parseInt(args[4]),
                    Double.parseDouble(args[5]),
                    Double.parseDouble(args[6])
            );
        } else {
            // Использовать значения по умолчанию
            myObject.demonstrateAllMethods();
        }
    }
}