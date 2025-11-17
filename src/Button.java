public class Button {
    private int clickCount;

    public Button() {
        this.clickCount = 0;
    }

    public void click() {
        clickCount++;
        System.out.println("Количество нажатий: " + clickCount);
    }

    // Геттер для тестирования
    public int getClickCount() {
        return clickCount;
    }
}
