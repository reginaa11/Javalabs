import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryAnalyzer {

    public static void main(String[] args) {
        try {
            // Читаем JSON файл
            Gson gson = new Gson();
            Type visitorListType = new TypeToken<List<Visitor>>(){}.getType();
            List<Visitor> visitors = gson.fromJson(new FileReader("books.json"), visitorListType);

            // Задание 1: Список посетителей и их количество
            System.out.println("=== ЗАДАНИЕ 1 ===");
            System.out.println("Список посетителей:");
            visitors.forEach(v -> System.out.println(v.getName() + " " + v.getSurname()));
            System.out.println("Общее количество посетителей: " + visitors.size());
            System.out.println();

            // Задание 2: Все уникальные книги
            System.out.println("=== ЗАДАНИЕ 2 ===");
            List<Book> allBooks = visitors.stream()
                    .flatMap(v -> v.getFavoriteBooks().stream())
                    .distinct()
                    .collect(Collectors.toList());

            System.out.println("Все уникальные книги:");
            allBooks.forEach(b -> System.out.println(b.getName() + " - " + b.getAuthor()));
            System.out.println("Количество уникальных книг: " + allBooks.size());
            System.out.println();

            // Задание 3: Сортировка книг по году издания
            System.out.println("=== ЗАДАНИЕ 3 ===");
            List<Book> sortedBooks = allBooks.stream()
                    .sorted(Comparator.comparingInt(Book::getPublishingYear))
                    .collect(Collectors.toList());

            System.out.println("Книги отсортированные по году издания:");
            sortedBooks.forEach(b -> System.out.println(b.getPublishingYear() + " - " + b.getName()));
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}