public class Main {
    public static void main(String[] args) {
        // Тестирование Button
        System.out.println("=== Тест Button ===");
        Button btn = new Button();
        btn.click();
        btn.click();

        // Тестирование Balance
        System.out.println("\n=== Тест Balance ===");
        Balance balance = new Balance();
        balance.addLeft(10);
        balance.addRight(5);
        System.out.println("Результат: " + balance.result());

        // Тестирование Bell
        System.out.println("\n=== Тест Bell ===");
        Bell bell = new Bell();
        bell.sound();
        bell.sound();
        bell.sound();

        // Тестирование OddEvenSeparator
        System.out.println("\n=== Тест OddEvenSeparator ===");
        OddEvenSeparator separator = new OddEvenSeparator();
        separator.addNumber(1);
        separator.addNumber(2);
        separator.addNumber(3);
        separator.addNumber(4);
        separator.even();
        separator.odd();

        // Тестирование Table
        System.out.println("\n=== Тест Table ===");
        Table table = new Table(2, 3);
        table.setValue(0, 0, 1);
        table.setValue(0, 1, 2);
        table.setValue(1, 0, 3);
        System.out.println("Таблица:\n" + table.toString());
        System.out.println("Среднее: " + table.average());
    }
}