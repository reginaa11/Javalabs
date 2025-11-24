import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShoeWarehouse {
    public static final List<String> SHOE_TYPES = List.of("Nike", "Adidas", "Puma", "Reebok");
    private final Queue<Order> orders = new LinkedList<>();
    private final int capacity;

    public ShoeWarehouse(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void receiveOrder(Order order) {
        while (orders.size() >= capacity) {
            try {
                System.out.println(Thread.currentThread().getName() + " ждёт: очередь заполнена");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Виртуальный поток прерван " + e);
            }
        }
        orders.add(order);
        System.out.println(Thread.currentThread().getName() + " добавил заказ: " + order);
        notifyAll();
    }

    public synchronized void fulfillOrder() {
        while (orders.isEmpty()) {
            try {
                System.out.println(Thread.currentThread().getName() + " ждёт: очередь пуста");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Order order = orders.poll();
        System.out.println(Thread.currentThread().getName() + " обработал заказ: " + order);
        notifyAll();
    }
}