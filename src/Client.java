import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;
    private boolean isRunning;
    private Thread messageReceiver;

    public void start() {
        try {
            System.out.println("Подключение к серверу " + SERVER_ADDRESS + ":" + SERVER_PORT + "...");
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
            isRunning = true;

            // Запуск потока для приема сообщений от сервера
            startMessageReceiver();

            // Обработка пользовательского ввода
            handleUserInput();

        } catch (ConnectException e) {
            System.err.println("Не удалось подключиться к серверу. Убедитесь, что сервер запущен.");
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void startMessageReceiver() {
        messageReceiver = new Thread(() -> {
            try {
                while (isRunning) {
                    String message = reader.readLine();
                    if (message == null) {
                        System.out.println("Соединение с сервером разорвано.");
                        isRunning = false;
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                if (isRunning) {
                    System.err.println("Ошибка при чтении сообщений: " + e.getMessage());
                }
            }
        });
        messageReceiver.start();
    }

    private void handleUserInput() {
        while (isRunning && scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.trim().isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("/exit")) {
                writer.println("/exit");
                isRunning = false;
                break;
            } else if (input.startsWith("/private") || input.startsWith("/all") || input.equals("/list")) {
                writer.println(input);
            } else {
                System.out.println("Используйте команды:");
                System.out.println("/list - показать список пользователей");
                System.out.println("/private [никнейм] [сообщение] - личное сообщение");
                System.out.println("/all [сообщение] - сообщение всем");
                System.out.println("/exit - выход");
            }
        }
    }

    private void disconnect() {
        isRunning = false;
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (scanner != null) {
                scanner.close();
            }
            if (messageReceiver != null && messageReceiver.isAlive()) {
                messageReceiver.join(1000);
            }
            System.out.println("Клиент отключен.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при отключении: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        // Обработка Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nЗавершение работы клиента...");
            client.disconnect();
        }));

        client.start();
    }
}