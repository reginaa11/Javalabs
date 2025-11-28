package lsp;

public class Penguin extends Bird {
    @SuppressWarnings("java:S106")
    public Penguin() {
        System.out.println("Создан пингвин");
    }

    @SuppressWarnings("java:S106")
    public void swim() {
        System.out.println(" Пингвин плавает");
    }
}