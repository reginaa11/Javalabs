public class OddRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i < 10; i+= 2){
            System.out.println(Thread.currentThread().getName() + " → нечётное " + i);
        }
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Виртуальный поток прерван " + e);
        }
    }
}