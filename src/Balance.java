public class Balance {
    private int leftWeight;
    private int rightWeight;

    public Balance() {
        this.leftWeight = 0;
        this.rightWeight = 0;
    }

    public void addLeft(int weight) {
        leftWeight += weight;
    }

    public void addRight(int weight) {
        rightWeight += weight;
    }

    public String result() {
        if (leftWeight == rightWeight) {
            return "=";
        } else if (rightWeight > leftWeight) {
            return "R";
        } else {
            return "L";
        }
    }
}