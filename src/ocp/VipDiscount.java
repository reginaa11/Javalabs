package ocp;

public class VipDiscount implements DiscountStrategy {
    public double applyDiscount(double price) {
        return price * 0.85;
    }
}