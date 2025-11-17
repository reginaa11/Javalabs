import java.util.*;

public class CollectionsOperations {
    public static void main(String[] args) {
        // Создаем массив из N случайных чисел
        int n = 10;
        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(101); // 0-100
        }
        System.out.println("Исходный массив: " + Arrays.toString(array));

        // Создаем список из массива
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        System.out.println("Список: " + list);

        // Сортируем по возрастанию
        Collections.sort(list);
        System.out.println("По возрастанию: " + list);

        // Сортируем в обратном порядке
        Collections.sort(list, Collections.reverseOrder());
        System.out.println("По убыванию: " + list);

        // Перемешиваем
        Collections.shuffle(list);
        System.out.println("Перемешанный: " + list);

        // Циклический сдвиг на 1 элемент
        Collections.rotate(list, 1);
        System.out.println("Со сдвигом: " + list);

        // Оставляем только уникальные элементы
        Set<Integer> uniqueSet = new HashSet<>(list);
        List<Integer> uniqueList = new ArrayList<>(uniqueSet);
        System.out.println("Уникальные: " + uniqueList);

        // Оставляем только дублирующиеся элементы
        List<Integer> duplicates = new ArrayList<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : list) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.add(entry.getKey());
            }
        }
        System.out.println("Дубликаты: " + duplicates);

        // Получаем массив из списка
        Integer[] newArray = list.toArray(new Integer[0]);
        System.out.println("Новый массив: " + Arrays.toString(newArray));

        // Подсчитываем количество вхождений
        System.out.println("Количество вхождений:");
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}