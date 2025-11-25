import java.util.ArrayList;
import java.util.List;

public class OddEvenSeparator {
    private List<Integer> numbers;

    public OddEvenSeparator() {
        numbers = new ArrayList<>();
    }

    public void addNumber(int number) {
        numbers.add(number);
    }

    public void even() {
        System.out.print("Четные числа: ");
        for (int num : numbers) {
            if (num % 2 == 0) {
                System.out.print(num + " ");
            }
        }
        System.out.println();
    }

    public void odd() {
        System.out.print("Нечетные числа: ");
        for (int num : numbers) {
            if (num % 2 != 0) {
                System.out.print(num + " ");
            }
        }
        System.out.println();
    }
}