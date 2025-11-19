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

            // Задание 4: Проверка наличия книг Jane Austen
            System.out.println("=== ЗАДАНИЕ 4 ===");
            boolean hasJaneAusten = visitors.stream()
                    .flatMap(v -> v.getFavoriteBooks().stream())
                    .anyMatch(b -> "Jane Austen".equals(b.getAuthor()));

            System.out.println("Есть ли книги Jane Austen в избранных: " + (hasJaneAusten ? "Да" : "Нет"));
            System.out.println();

            // Задание 5: Максимальное количество книг у одного посетителя
            System.out.println("=== ЗАДАНИЕ 5 ===");
            Optional<Integer> maxBooks = visitors.stream()
                    .map(v -> v.getFavoriteBooks().size())
                    .max(Integer::compareTo);

            System.out.println("Максимальное количество книг у одного посетителя: " +
                    maxBooks.orElse(0));
            System.out.println();

            // Задание 6: SMS сообщения для подписанных пользователей
            System.out.println("=== ЗАДАНИЕ 6 ===");

            // Вычисляем среднее количество книг
            double averageBooks = visitors.stream()
                    .mapToInt(v -> v.getFavoriteBooks().size())
                    .average()
                    .orElse(0.0);

            System.out.println("Среднее количество книг на посетителя: " + averageBooks);

            // Создаем SMS сообщения для подписанных пользователей
            List<SmsMessage> smsMessages = visitors.stream()
                    .filter(Visitor::isSubscribed)
                    .map(v -> {
                        int bookCount = v.getFavoriteBooks().size();
                        String message;

                        if (bookCount > averageBooks) {
                            message = "you are a bookworm";
                        } else if (bookCount < averageBooks) {
                            message = "read more";
                        } else {
                            message = "fine";
                        }

                        return new SmsMessage(v.getPhone(), message);
                    })
                    .collect(Collectors.toList());

            System.out.println("SMS сообщения для подписанных пользователей:");
            smsMessages.forEach(sms ->
                    System.out.println(sms.getPhoneNumber() + ": " + sms.getMessage()));




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}