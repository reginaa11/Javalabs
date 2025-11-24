public class EvenThread extends Thread {
    @Override
    public void run() {
        for (int i = 2; i <= 10; i += 2) {
            System.out.println(Thread.currentThread().getName() + " → чётное: " + i);
        }
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Виртуальный поток прерван " + e);
            }
        });
    }
}