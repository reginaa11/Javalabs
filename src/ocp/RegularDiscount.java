package ocp;

public class RegularDiscount implements DiscountStrategy {
    public double applyDiscount(double price) {
        return price * 0.95;
    }
}