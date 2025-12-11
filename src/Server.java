import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 8080;
    private static final int MAX_THREADS = 10;

    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private Map<String, ClientHandler> clients;
    private boolean isRunning;
    private SimpleDateFormat dateFormat;

    public Server() {
        this.clients = new ConcurrentHashMap<>();
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        this.isRunning = true;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Создаем директорию для логов
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            logInfo("Сервер запущен на порту " + PORT);
            logInfo("Ожидание подключений...");

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                logInfo("Новое подключение: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                threadPool.execute(clientHandler);
            }

        } catch (IOException e) {
            if (isRunning) {
                logError("Ошибка сервера: " + e.getMessage());
            }
        } finally {
            stop();
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logError("Ошибка при остановке сервера: " + e.getMessage());
        }

        threadPool.shutdown();
        logInfo("Сервер остановлен");
    }

    public synchronized void addClient(String nickname, ClientHandler handler) {
        clients.put(nickname, handler);
        logInfo("Добавлен клиент: " + nickname);
        logInfo("Всего клиентов: " + clients.size());
    }

    public synchronized void removeClient(String nickname) {
        clients.remove(nickname);
        logInfo("Удален клиент: " + nickname);
        logInfo("Всего клиентов: " + clients.size());
    }

    public synchronized boolean isNicknameTaken(String nickname) {
        return clients.containsKey(nickname);
    }

    public synchronized List<String> getConnectedUsers() {
        return new ArrayList<>(clients.keySet());
    }

    public synchronized ClientHandler getClientHandler(String nickname) {
        return clients.get(nickname);
    }

    public synchronized void broadcastMessage(String message, String sender) {
        for (ClientHandler client : clients.values()) {
            if (!client.getNickname().equals(sender)) {
                client.sendMessage(message);
            }
        }
        logChatMessage("ВСЕМ от " + sender + ": " + message);
    }

    public synchronized void sendPrivateMessage(String sender, String recipient, String message) {
        ClientHandler recipientHandler = clients.get(recipient);
        if (recipientHandler != null) {
            recipientHandler.sendMessage("[" + sender + " -> " + recipient + "]: " + message);
            logChatMessage("ЛИЧНОЕ " + sender + " -> " + recipient + ": " + message);
        }
    }

    private void logInfo(String message) {
        System.out.println(dateFormat.format(new Date()) + " [INFO] " + message);
        logToFile("INFO", message);
    }

    private void logError(String message) {
        System.err.println(dateFormat.format(new Date()) + " [ERROR] " + message);
        logToFile("ERROR", message);
    }

    private void logChatMessage(String message) {
        logToFile("CHAT", message);
    }

    private void logToFile(String level, String message) {
        try {
            String logEntry = dateFormat.format(new Date()) + " [" + level + "] " + message;

            // Общий лог сервера
            try (PrintWriter writer = new PrintWriter(new FileWriter("logs/server.log", true))) {
                writer.println(logEntry);
            }

            // Лог сообщений чата
            if ("CHAT".equals(level)) {
                try (PrintWriter chatWriter = new PrintWriter(new FileWriter("logs/chat_messages.log", true))) {
                    chatWriter.println(dateFormat.format(new Date()) + " | " + message);
                }
            }

            // Лог ошибок
            if ("ERROR".equals(level) || "WARN".equals(level)) {
                try (PrintWriter errorWriter = new PrintWriter(new FileWriter("logs/errors.log", true))) {
                    errorWriter.println(logEntry);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в лог-файл: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

        // Обработка Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nПолучен сигнал завершения работы...");
            server.stop();
        }));

        server.start();
    }
}