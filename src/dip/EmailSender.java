package dip;

public class EmailSender implements MessageSender {
    @SuppressWarnings("java:S106")
    public void sendMessage(String message) {
        System.out.println(" Отправка email: " + message);
    }
}