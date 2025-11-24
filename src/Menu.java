import java.util.Scanner;
import java.util.concurrent.*;

@SuppressWarnings("java:S106") // Подавляем предупреждение для всего класса
public class Menu {
    private Menu() {
        throw new IllegalStateException("Utility class");
    }

    public static void show() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ShoeWarehouse warehouse = new ShoeWarehouse(5);
        while (true) {
            System.out.println("\n=== Меню лабораторной работы ===");
            System.out.println("1. Создание потоков");
            System.out.println("2. Producer–Consumer");
            System.out.println("3. ExecutorService(с прогресс-баром)");
            System.out.println("0. Выход");
            System.out.println("Выберите пункт: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> runThreadsExample();
                case 2 -> runProducerConsumerExample(warehouse);
                case 3 -> runExecutorExample();
                case 0 -> {
                    System.out.println("Выход из программы...");
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private static void runThreadsExample() throws InterruptedException {
        System.out.println("\n=== Задание 1: Создание потоков ===");
        Thread evenThread = new EvenThread();
        Thread oddThread = new Thread(new OddRunnable());
        evenThread.start();
        oddThread.start();
        evenThread.join();
        oddThread.join();
    }

    private static void runProducerConsumerExample(ShoeWarehouse warehouse) throws InterruptedException {
        System.out.println("\n=== Задание 2: Producer–Consumer ===");
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                Order order = new Order(i, ShoeWarehouse.SHOE_TYPES.get(i % ShoeWarehouse.SHOE_TYPES.size()), (i + 1) * 2);
                warehouse.receiveOrder(order);
                sleepShort(150);
            }
        }, "Producer");
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                warehouse.fulfillOrder();
                sleepShort(200);
            }
        }, "Consumer");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    private static void runExecutorExample() throws InterruptedException {
        System.out.println("\n=== Задание 3: ExecutorService с прогресс-баром ===");
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            ShoeWarehouse warehouse = new ShoeWarehouse(5);
            int totalOrders = 10;
            Progress progress = new Progress(totalOrders);
            Thread progressThread = new Thread(() -> {
                while (!progress.isComplete()) {
                    progress.render();
                    sleepShort(100);
                }
                progress.render();
            });
            progressThread.start();
            for (int i = 1; i <= totalOrders; i++) {
                Order order = new Order(i, ShoeWarehouse.SHOE_TYPES.get(i % ShoeWarehouse.SHOE_TYPES.size()), (i + 1) * 2);
                executor.submit(() -> {
                    warehouse.receiveOrder(order);
                    System.out.println(Thread.currentThread().getName() + " добавил заказ: " + order);
                    sleepShort(150);
                });
                executor.submit(() -> {
                    warehouse.fulfillOrder();
                    progress.increment();
                    sleepShort(250);
                });
            }
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Some tasks did not complete in time");
            }
            progress.complete();
            progressThread.join();
            System.out.println("\nВсе заказы обработаны.");
        }
    }

    private static void sleepShort(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Виртуальный поток прерван" + e);
        }
    }

    @SuppressWarnings("java:S106") // Подавляем для вложенного класса Progress
    private static class Progress {
        private final int total;
        private int current = 0;
        private volatile boolean done = false;

        public Progress(int total) {
            this.total = total;
        }

        public synchronized void increment() {
            current++;
        }

        public boolean isComplete() {
            return done;
        }

        public synchronized void complete() {
            done = true;
        }

        public synchronized void render() {
            int percent = (int) ((current / (double) total) * 100);
            int bars = percent / 10;
            StringBuilder bar = new StringBuilder("\r[#");
            for (int i = 1; i < 10; i++) {
                bar.append(i < bars ? "#" : "-");
            }
            bar.append("] ").append(percent).append("%");
            System.out.println("\033[F");
            System.out.println(bar + "\n");
            System.out.flush();
        }
    }
}