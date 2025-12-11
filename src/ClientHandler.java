import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Server server;
    private String nickname;
    private boolean isConnected;
    private SimpleDateFormat dateFormat;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.isConnected = true;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Аутентификация
            authenticate();

            // Основной цикл обработки сообщений
            processMessages();

        } catch (IOException e) {
            logError("Ошибка при работе с клиентом " + nickname + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void authenticate() throws IOException {
        while (true) {
            writer.println("Введите ваш никнейм (или 'exit' для выхода):");
            String input = reader.readLine();

            if (input == null) {
                throw new IOException("Соединение разорвано");
            }

            if (input.equalsIgnoreCase("exit")) {
                writer.println("До свидания!");
                isConnected = false;
                return;
            }

            if (input.trim().isEmpty()) {
                writer.println("Никнейм не может быть пустым.");
                continue;
            }

            synchronized (server) {
                if (server.isNicknameTaken(input)) {
                    writer.println("Никнейм уже занят. Попробуйте другой.");
                } else {
                    nickname = input;
                    server.addClient(nickname, this);
                    writer.println("Добро пожаловать, " + nickname + "!");
                    writer.println("Доступные команды:");
                    writer.println("/list - показать список пользователей");
                    writer.println("/private [никнейм] [сообщение] - отправить личное сообщение");
                    writer.println("/all [сообщение] - отправить всем");
                    writer.println("/exit - выйти из чата");
                    server.broadcastMessage(nickname + " присоединился к чату!", nickname);
                    logInfo("Клиент аутентифицирован: " + nickname);
                    break;
                }
            }
        }
    }

    private void processMessages() throws IOException {
        while (isConnected) {
            String message = reader.readLine();
            if (message == null) {
                break;
            }

            if (message.startsWith("/exit")) {
                writer.println("До свидания!");
                break;
            } else if (message.startsWith("/list")) {
                sendUserList();
            } else if (message.startsWith("/private")) {
                handlePrivateMessage(message);
            } else if (message.startsWith("/all")) {
                handleBroadcastMessage(message);
            } else {
                writer.println("Неизвестная команда. Используйте /list для просмотра команд.");
            }
        }
    }

    private void sendUserList() {
        List<String> users = server.getConnectedUsers();  // Теперь работает с правильным импортом
        StringBuilder userList = new StringBuilder("Подключенные пользователи (" + users.size() + "):\n");
        for (String user : users) {
            userList.append("- ").append(user).append("\n");
        }
        writer.println(userList.toString());
    }

    private void handlePrivateMessage(String message) {
        try {
            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                writer.println("Использование: /private [никнейм] [сообщение]");
                return;
            }

            String recipient = parts[1];
            String msg = parts[2];

            if (recipient.equals(nickname)) {
                writer.println("Нельзя отправить сообщение самому себе.");
                return;
            }

            server.sendPrivateMessage(nickname, recipient, msg);
            writer.println("[" + nickname + " -> " + recipient + "]: " + msg);
            logInfo("Личное сообщение от " + nickname + " к " + recipient + ": " + msg);

        } catch (Exception e) {
            writer.println("Ошибка при отправке личного сообщения.");
            logError("Ошибка при обработке личного сообщения от " + nickname + ": " + e.getMessage());
        }
    }

    private void handleBroadcastMessage(String message) {
        try {
            String msg = message.substring(4).trim();
            if (msg.isEmpty()) {
                writer.println("Сообщение не может быть пустым.");
                return;
            }

            server.broadcastMessage("[" + nickname + " -> ALL]: " + msg, nickname);
            writer.println("[" + nickname + " -> ALL]: " + msg);
            logInfo("Широковещательное сообщение от " + nickname + ": " + msg);

        } catch (Exception e) {
            writer.println("Ошибка при отправке сообщения.");
            logError("Ошибка при обработке широковещательного сообщения от " + nickname + ": " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public String getNickname() {
        return nickname;
    }

    private void disconnect() {
        if (nickname != null) {
            server.removeClient(nickname);
            server.broadcastMessage(nickname + " покинул чат.", nickname);
            logInfo("Клиент отключен: " + nickname);
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logError("Ошибка при закрытии сокета: " + e.getMessage());
        }

        isConnected = false;
    }

    private void logInfo(String message) {
        System.out.println(dateFormat.format(new Date()) + " [INFO] " + message);
        logToFile("INFO", message);
    }

    private void logError(String message) {
        System.err.println(dateFormat.format(new Date()) + " [ERROR] " + message);
        logToFile("ERROR", message);
    }

    private void logToFile(String level, String message) {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            try (PrintWriter logWriter = new PrintWriter(new FileWriter("logs/server.log", true))) {
                logWriter.println(dateFormat.format(new Date()) + " [" + level + "] " + message);
            }

            // Логирование сообщений чата в отдельный файл
            if (message.contains("сообщение") || message.contains("присоединился") || message.contains("покинул")) {
                try (PrintWriter chatWriter = new PrintWriter(new FileWriter("logs/chat_messages.log", true))) {
                    chatWriter.println(dateFormat.format(new Date()) + " | " + message);
                }
            }

            // Логирование ошибок в отдельный файл
            if ("ERROR".equals(level) || "WARN".equals(level)) {
                try (PrintWriter errorWriter = new PrintWriter(new FileWriter("logs/errors.log", true))) {
                    errorWriter.println(dateFormat.format(new Date()) + " [" + level + "] " + message);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в лог-файл: " + e.getMessage());
        }
    }
}