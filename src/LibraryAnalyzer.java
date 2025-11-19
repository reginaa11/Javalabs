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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}