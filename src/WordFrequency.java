import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        String text = "Hello world hello Java world java programming";

        // Приводим к нижнему регистру и разбиваем на слова
        String[] words = text.toLowerCase().split("\\s+");

        // Подсчитываем частоту слов
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        // Выводим результат
        System.out.println("Частота слов:");
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}