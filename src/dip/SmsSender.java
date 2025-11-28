package dip;

public class SmsSender implements MessageSender {
    @SuppressWarnings("java:S106")
    public void sendMessage(String message) {
        System.out.println(" Отправка SMS: " + message);
    }
}