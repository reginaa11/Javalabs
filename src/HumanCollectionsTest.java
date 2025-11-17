import java.util.*;

public class HumanCollectionsTest {
    public static void main(String[] args) {
        // Создаем список людей
        List<Human> humans = Arrays.asList(
                new Human("Иван", "Иванов", 25),
                new Human("Петр", "Петров", 30),
                new Human("Анна", "Сидорова", 22),
                new Human("Мария", "Иванова", 25)
        );

        // HashSet
        Set<Human> hashSet = new HashSet<>(humans);
        System.out.println("HashSet: " + hashSet);

        // LinkedHashSet
        Set<Human> linkedHashSet = new LinkedHashSet<>(humans);
        System.out.println("LinkedHashSet: " + linkedHashSet);

        // TreeSet (сортировка по возрасту - Comparable)
        Set<Human> treeSet = new TreeSet<>(humans);
        System.out.println("TreeSet (по возрасту): " + treeSet);

        // TreeSet с компаратором по фамилии
        Set<Human> treeSetByLastName = new TreeSet<>(new HumanComparatorByLastName());
        treeSetByLastName.addAll(humans);
        System.out.println("TreeSet (по фамилии): " + treeSetByLastName);

        // TreeSet с анонимным компаратором по возрасту
        Set<Human> treeSetByAge = new TreeSet<>(new Comparator<Human>() {
            @Override
            public int compare(Human h1, Human h2) {
                return h1.getAge() - h2.getAge();
            }
        });
        treeSetByAge.addAll(humans);
        System.out.println("TreeSet (анонимный по возрасту): " + treeSetByAge);

        // Различия в выводах коллекций:
        System.out.println("\nРазличия коллекций:");
        System.out.println("HashSet - не сохраняет порядок, быстрый доступ");
        System.out.println("LinkedHashSet - сохраняет порядок добавления");
        System.out.println("TreeSet - сортирует элементы (по умолчанию по возрасту)");
        System.out.println("TreeSet с компаратором - сортирует по указанному правилу");
    }
}