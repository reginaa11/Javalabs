import java.util.*;

public class PrimesGeneratorTest {
    public static void main(String[] args) {
        int n = 10;
        PrimesGenerator generator = new PrimesGenerator(n);

        // Прямой порядок
        System.out.println("Простые числа в прямом порядке:");
        for (int prime : generator) {
            System.out.print(prime + " ");
        }
        System.out.println();

        // Обратный порядок
        List<Integer> primesList = new ArrayList<>();
        for (int prime : new PrimesGenerator(n)) {
            primesList.add(prime);
        }
        Collections.reverse(primesList);
        System.out.println("Простые числа в обратном порядке:");
        for (int prime : primesList) {
            System.out.print(prime + " ");
        }
        System.out.println();
    }
}