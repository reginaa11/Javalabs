import java.util.*;

public class MapUtils {
    public static <K, V> Map<V, K> swapMap(Map<K, V> originalMap) {
        Map<V, K> swappedMap = new HashMap<>();
        for (Map.Entry<K, V> entry : originalMap.entrySet()) {
            swappedMap.put(entry.getValue(), entry.getKey());
        }
        return swappedMap;
    }

    public static void main(String[] args) {
        // Тестируем метод
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);
        original.put("three", 3);

        System.out.println("Исходная мапа: " + original);

        Map<Integer, String> swapped = swapMap(original);
        System.out.println("Обменянная мапа: " + swapped);
    }
}