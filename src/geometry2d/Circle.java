package geometry2d;

public class Circle implements Figure {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return String.format("Круг [радиус=%.2f, площадь=%.2f, периметр=%.2f]",
                radius, area(), perimeter());
    }
}