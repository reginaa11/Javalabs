import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;

public class Main {

    private static final String URL = "jdbc:h2:mem:lab9db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            // Явная регистрация драйвера H2
            Class.forName("org.h2.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Подключение к H2 установлено!\n");

                // Задание 1-3: Работа с таблицей music
                task1to3(conn);

                // Задание 4: Работа с JSON данными
                task4(conn);

            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void task1to3(Connection conn) throws SQLException {
        System.out.println("=== ЗАДАНИЕ 1-3: РАБОТА С ТАБЛИЦЕЙ MUSIC ===");

        // Создание таблицы music по образцу из music-create.sql
        String createMusicSQL = """
            CREATE TABLE IF NOT EXISTS music (
                id INT PRIMARY KEY,
                name TEXT NOT NULL
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createMusicSQL);
            System.out.println("Таблица music создана");
        }

        // Добавление данных из music-create.sql
        String insertMusicSQL = """
            INSERT INTO music (id, name) VALUES 
            (1, 'Bohemian Rhapsody'),
            (2, 'Stairway to Heaven'),
            (3, 'Imagine'),
            (4, 'Sweet Child O Mine'),
            (5, 'Hey Jude'),
            (6, 'Hotel California'),
            (7, 'Billie Jean'),
            (8, 'Wonderwall'),
            (9, 'Smells Like Teen Spirit'),
            (10, 'Let It Be'),
            (11, 'I Want It All'),
            (12, 'November Rain'),
            (13, 'Losing My Religion'),
            (14, 'One'),
            (15, 'With or Without You'),
            (16, 'Sweet Caroline'),
            (17, 'Yesterday'),
            (18, 'Dont Stop Believin'),
            (19, 'Crazy Train'),
            (20, 'Always')
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertMusicSQL);
            System.out.println("Данные добавлены в таблицу music");
        } catch (SQLException e) {
            System.out.println("Данные уже существуют в таблице music");
        }

        // Задание 1: Получить список музыкальных композиций
        System.out.println("\n--- Задание 1: Все музыкальные композиции ---");
        String selectAllSQL = "SELECT * FROM music";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSQL)) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name"));
            }
        }

        // Задание 2: Композиции без букв m и t
        System.out.println("\n--- Задание 2: Композиции без букв m и t ---");
        String filterSQL = "SELECT * FROM music WHERE LOWER(name) NOT LIKE '%m%' AND LOWER(name) NOT LIKE '%t%'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(filterSQL)) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name"));
            }
        }

        // Задание 3: Добавить любимую композицию
        System.out.println("\n--- Задание 3: Добавление любимой композиции ---");
        String insertSQL = "INSERT INTO music (id, name) VALUES (?, ?)";
        String mySong = "Shape of You";
        int newId = 21;

        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            ps.setInt(1, newId);
            ps.setString(2, mySong);
            ps.executeUpdate();
            System.out.println("Добавлена композиция: " + mySong + " (id: " + newId + ")");
        } catch (SQLException e) {
            System.out.println("Композиция уже существует");
        }
    }

    private static void task4(Connection conn) throws Exception {
        System.out.println("\n=== ЗАДАНИЕ 4: РАБОТА С JSON ДАННЫМИ ===");

        // Создание таблиц для посетителей и книг
        String createVisitorsSQL = """
            CREATE TABLE IF NOT EXISTS visitors (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                surname VARCHAR(50) NOT NULL,
                phone VARCHAR(20),
                subscribed BOOLEAN,
                UNIQUE(name, surname)
            )
            """;

        String createBooksSQL = """
            CREATE TABLE IF NOT EXISTS books (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                author VARCHAR(255) NOT NULL,
                publishing_year INT,
                isbn VARCHAR(20),
                publisher VARCHAR(255),
                UNIQUE(name, author)
            )
            """;

        String createVisitorBooksSQL = """
            CREATE TABLE IF NOT EXISTS visitor_books (
                visitor_id INT,
                book_id INT,
                FOREIGN KEY (visitor_id) REFERENCES visitors(id),
                FOREIGN KEY (book_id) REFERENCES books(id),
                PRIMARY KEY (visitor_id, book_id)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createVisitorsSQL);
            stmt.execute(createBooksSQL);
            stmt.execute(createVisitorBooksSQL);
            System.out.println("Таблицы visitors, books, visitor_books созданы");
        }

        // Чтение JSON файла
        Gson gson = new Gson();
        Type visitorListType = new TypeToken<List<Visitor>>(){}.getType();
        List<Visitor> visitors = gson.fromJson(new FileReader("books.json"), visitorListType);

        // Вставка уникальных посетителей и книг
        String insertVisitorSQL = "INSERT INTO visitors (name, surname, phone, subscribed) VALUES (?, ?, ?, ?)";
        String insertBookSQL = "INSERT INTO books (name, author, publishing_year, isbn, publisher) VALUES (?, ?, ?, ?, ?)";
        String insertVisitorBookSQL = "INSERT INTO visitor_books (visitor_id, book_id) VALUES (?, ?)";

        for (Visitor visitor : visitors) {
            // Вставка посетителя
            int visitorId;
            try (PreparedStatement ps = conn.prepareStatement(insertVisitorSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, visitor.getName());
                ps.setString(2, visitor.getSurname());
                ps.setString(3, visitor.getPhone());
                ps.setBoolean(4, visitor.isSubscribed());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        visitorId = rs.getInt(1);
                    } else {
                        continue;
                    }
                }
            } catch (SQLException e) {
                // Если посетитель уже существует, получаем его ID
                String selectVisitorSQL = "SELECT id FROM visitors WHERE name = ? AND surname = ?";
                try (PreparedStatement ps = conn.prepareStatement(selectVisitorSQL)) {
                    ps.setString(1, visitor.getName());
                    ps.setString(2, visitor.getSurname());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            visitorId = rs.getInt("id");
                        } else {
                            continue;
                        }
                    }
                }
            }

            // Вставка уникальных книг и связей
            for (Book book : visitor.getFavoriteBooks()) {
                int bookId;
                try (PreparedStatement ps = conn.prepareStatement(insertBookSQL, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, book.getName());
                    ps.setString(2, book.getAuthor());
                    ps.setInt(3, book.getPublishingYear());
                    ps.setString(4, book.getIsbn());
                    ps.setString(5, book.getPublisher());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            bookId = rs.getInt(1);
                        } else {
                            continue;
                        }
                    }
                } catch (SQLException e) {
                    // Если книга уже существует, получаем её ID
                    String selectBookSQL = "SELECT id FROM books WHERE name = ? AND author = ?";
                    try (PreparedStatement ps = conn.prepareStatement(selectBookSQL)) {
                        ps.setString(1, book.getName());
                        ps.setString(2, book.getAuthor());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                bookId = rs.getInt("id");
                            } else {
                                continue;
                            }
                        }
                    }
                }

                // Связь посетитель-книга
                try (PreparedStatement ps = conn.prepareStatement(insertVisitorBookSQL)) {
                    ps.setInt(1, visitorId);
                    ps.setInt(2, bookId);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    // Связь уже существует
                }
            }
        }

        System.out.println("Данные из books.json успешно загружены в БД");

        // Вывод статистики
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM visitors");
            if (rs.next()) {
                System.out.println("Количество посетителей: " + rs.getInt(1));
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM books");
            if (rs.next()) {
                System.out.println("Количество уникальных книг: " + rs.getInt(1));
            }
        }
    }
}