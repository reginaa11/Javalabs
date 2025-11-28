package isp;

public class OldPrinter implements Printer {
    @SuppressWarnings("java:S106")
    public void print() {
        System.out.println("  Старый принтер: Печатает документ");
    }
}