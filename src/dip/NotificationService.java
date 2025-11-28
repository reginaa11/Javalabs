package dip;

public class NotificationService {
    private final MessageSender messageSender;

    public NotificationService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @SuppressWarnings("java:S106")
    public void sendNotification(String message) {
        System.out.println("Сервис уведомлений:");
        messageSender.sendMessage(message);
    }
}