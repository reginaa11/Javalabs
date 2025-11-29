import java.sql.*;

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

                // Задание 1: Работа с таблицей music
                task1(conn);

            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void task1(Connection conn) throws SQLException {
        System.out.println("=== ЗАДАНИЕ 1: РАБОТА С ТАБЛИЦЕЙ MUSIC ===");

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
    }
}