import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;

public class Main {

    private static final String URL = "jdbc:h2:mem:lab9db";
    private static final String USER = "sa";
    private static final String PASSWORD = "secure_password_123";

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

                // Задание 5-6: Запросы к книгам
                task5to6(conn);

                // Задание 7: Добавление информации о себе
                task7(conn);

                // Задание 8: Удаление таблиц
                task8(conn);

            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
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

    private static void task5to6(Connection conn) throws SQLException {
        System.out.println("\n=== ЗАДАНИЕ 5-6: ЗАПРОСЫ К КНИГАМ ===");

        // Задание 5: Отсортированный список книг по году издания
        System.out.println("--- Задание 5: Книги отсортированные по году издания ---");
        String sortedBooksSQL = "SELECT name, author, publishing_year FROM books ORDER BY publishing_year";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sortedBooksSQL)) {

            while (rs.next()) {
                System.out.println(rs.getInt("publishing_year") + " - " +
                        rs.getString("name") + " (" + rs.getString("author") + ")");
            }
        }

        // Задание 6: Книги младше 2000 года
        System.out.println("\n--- Задание 6: Книги младше 2000 года ---");
        String youngBooksSQL = "SELECT name, author, publishing_year FROM books WHERE publishing_year < 2000 ORDER BY publishing_year";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(youngBooksSQL)) {

            while (rs.next()) {
                System.out.println(rs.getInt("publishing_year") + " - " +
                        rs.getString("name") + " (" + rs.getString("author") + ")");
            }
        }
    }

    private static void task7(Connection conn) throws SQLException {
        System.out.println("\n=== ЗАДАНИЕ 7: ДОБАВЛЕНИЕ ИНФОРМАЦИИ О СЕБЕ ===");

        // Добавляем себя как посетителя
        String insertMeSQL = "INSERT INTO visitors (name, surname, phone, subscribed) VALUES (?, ?, ?, ?)";
        int myId;

        try (PreparedStatement ps = conn.prepareStatement(insertMeSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Regina");
            ps.setString(2, "Simonenko");
            ps.setString(3, "+79991234567");
            ps.setBoolean(4, true);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    myId = rs.getInt(1);
                    System.out.println("Добавлен посетитель: Regina Simonenko (id: " + myId + ")");
                } else {
                    return;
                }
            }
        } catch (SQLException e) {
            // Если уже существует, получаем ID
            String selectVisitorSQL = "SELECT id FROM visitors WHERE name = 'Regina' AND surname = 'Simonenko'";
            try (PreparedStatement ps = conn.prepareStatement(selectVisitorSQL);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    myId = rs.getInt("id");
                    System.out.println("Посетитель уже существует (id: " + myId + ")");
                } else {
                    return;
                }
            }
        }

        // Добавляем мои любимые книги
        String[][] myBooks = {
                {"Clean Code", "Robert Martin", "2008", "9780132350884", "Prentice Hall"},
                {"Effective Java", "Joshua Bloch", "2018", "9780134686097", "Addison-Wesley"},
                {"Head First Design Patterns", "Eric Freeman", "2004", "9780596007126", "O'Reilly Media"}
        };

        String insertBookSQL = "INSERT INTO books (name, author, publishing_year, isbn, publisher) VALUES (?, ?, ?, ?, ?)";
        String linkBookSQL = "INSERT INTO visitor_books (visitor_id, book_id) VALUES (?, ?)";

        for (String[] book : myBooks) {
            int bookId;

            // Добавляем книгу (или получаем ID если уже существует)
            try (PreparedStatement ps = conn.prepareStatement(insertBookSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, book[0]);
                ps.setString(2, book[1]);
                ps.setInt(3, Integer.parseInt(book[2]));
                ps.setString(4, book[3]);
                ps.setString(5, book[4]);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        bookId = rs.getInt(1);
                        System.out.println("Добавлена книга: " + book[0]);
                    } else {
                        continue;
                    }
                }
            } catch (SQLException e) {
                // Книга уже существует
                String selectBookSQL = "SELECT id FROM books WHERE name = ? AND author = ?";
                try (PreparedStatement ps = conn.prepareStatement(selectBookSQL)) {
                    ps.setString(1, book[0]);
                    ps.setString(2, book[1]);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            bookId = rs.getInt("id");
                            System.out.println("Книга уже существует: " + book[0]);
                        } else {
                            continue;
                        }
                    }
                }
            }

            // Связываем книгу со мной
            try (PreparedStatement ps = conn.prepareStatement(linkBookSQL)) {
                ps.setInt(1, myId);
                ps.setInt(2, bookId);
                ps.executeUpdate();
                System.out.println("Связь создана: Regina Simonenko -> " + book[0]);
            } catch (SQLException e) {
                System.out.println("Связь уже существует");
            }
        }

        // Выводим обратно информацию о себе
        System.out.println("\n--- Мои данные и любимые книги ---");
        String myDataSQL = """
            SELECT v.name, v.surname, b.name as book_name, b.author, b.publishing_year 
            FROM visitors v
            JOIN visitor_books vb ON v.id = vb.visitor_id
            JOIN books b ON vb.book_id = b.id
            WHERE v.name = 'Regina' AND v.surname = 'Simonenko'
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(myDataSQL)) {

            boolean first = true;
            while (rs.next()) {
                if (first) {
                    System.out.println("Посетитель: " + rs.getString("name") + " " + rs.getString("surname"));
                    System.out.println("Любимые книги:");
                    first = false;
                }
                System.out.println("  - " + rs.getString("book_name") + " (" +
                        rs.getString("author") + ", " + rs.getInt("publishing_year") + ")");
            }
        }
    }

    private static void task8(Connection conn) throws SQLException {
        System.out.println("\n=== ЗАДАНИЕ 8: УДАЛЕНИЕ ТАБЛИЦ ===");

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS visitor_books");
            stmt.execute("DROP TABLE IF EXISTS visitors");
            stmt.execute("DROP TABLE IF EXISTS books");
            System.out.println("Таблицы visitors, books, visitor_books удалены");
        }
    }
}