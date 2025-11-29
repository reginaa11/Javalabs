import java.sql.*;

public class Main {

    private static final String URL = "jdbc:h2:mem:lab9db;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Подключение к H2 установлено!\n");

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}