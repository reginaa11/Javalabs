package geometry3d;
import geometry2d.Figure;

public class Cylinder {
    private Figure base;
    private double height;

    public Cylinder(Figure base, double height) {
        this.base = base;
        this.height = height;
    }

    public double volume() {
        return base.area() * height;
    }

    @Override
    public String toString() {
        return String.format("Цилиндр [основание=%s, высота=%.2f, объем=%.2f]",
                base.toString(), height, volume());
    }
}
