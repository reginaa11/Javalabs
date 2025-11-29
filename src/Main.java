import java.sql.*;

public class Main {

    private static final String URL = "jdbc:h2:mem:lab9db;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Подключение к H2 установлено!\n");

            // Задание 1: Создание таблицы music и вывод данных
            task1(conn);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static void task1(Connection conn) throws SQLException {
        System.out.println("=== ЗАДАНИЕ 1: СОЗДАНИЕ ТАБЛИЦЫ MUSIC И ВЫВОД ДАННЫХ ===");

        // Создание таблицы music
        String createMusicSQL = "CREATE TABLE IF NOT EXISTS music (id INT PRIMARY KEY, name TEXT NOT NULL)";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createMusicSQL);
            System.out.println("Таблица music создана");
        }

        // Добавление данных
        String insertMusicSQL = "INSERT INTO music (id, name) VALUES (1, 'Bohemian Rhapsody')";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertMusicSQL);
            System.out.println("Данные добавлены в таблицу music");
        }

        // Вывод всех музыкальных композиций
        System.out.println("\nСписок музыкальных композиций:");
        String selectAllSQL = "SELECT * FROM music";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSQL)) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name"));
            }
        }
    }
}