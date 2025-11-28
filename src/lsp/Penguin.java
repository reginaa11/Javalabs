package lsp;

public class Penguin extends Bird {
    // Penguin может есть, но не летать - не нарушает LSP
    public void swim() {
        System.out.println("Пингвин плавает");
    }
}