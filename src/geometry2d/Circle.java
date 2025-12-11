package geometry2d;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Shape {
    private double radius;

    public Circle(double x, double y, double radius, Color color) {
        super(x, y, color);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillOval(getX() - radius, getY() - radius, radius * 2, radius * 2);
    }

    @Override
    public boolean contains(double mx, double my) {
        double dx = mx - getX();
        double dy = my - getY();
        return dx * dx + dy * dy <= radius * radius;
    }

    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
}