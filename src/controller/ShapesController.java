package controller;

import geometry2d.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import app.Main;

import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;

public class ShapesController {

    @FXML private Canvas canvas;
    private final List<Shape> shapes = new ArrayList<>();
    private Shape selectedShape = null;
    private double offsetX, offsetY;
    private final SecureRandom random = new SecureRandom();

    @FXML
    private void initialize() {
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(e -> selectedShape = null);
        redraw();

    }

    private void mousePressed(MouseEvent e) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (shape.contains(e.getX(), e.getY())) {
                selectedShape = shape;
                offsetX = e.getX() - shape.getX();
                offsetY = e.getY() - shape.getY();
                shapes.remove(shape);
                shapes.add(shape);

                if (e.getButton() == MouseButton.SECONDARY) {
                    shape.setColor(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
                    selectedShape = null;
                }
                redraw();
                return;
            }
        }
    }

    private void mouseDragged(MouseEvent e) {
        if (selectedShape != null) {
            selectedShape.moveTo(e.getX() - offsetX, e.getY() - offsetY);
            redraw();
        }
    }

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }

    @FXML
    private void addCircle() {
        double r = 20 + random.nextDouble() * 40;
        double x = r + random.nextDouble() * (canvas.getWidth() - 2 * r);
        double y = r + random.nextDouble() * (canvas.getHeight() - 2 * r);
        shapes.add(new Circle(x, y, r, Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())));
        redraw();
    }

    @FXML
    private void addRect() {
        double w = 30 + random.nextDouble() * 70;
        double h = 30 + random.nextDouble() * 70;
        double x = random.nextDouble() * (canvas.getWidth() - w);
        double y = random.nextDouble() * (canvas.getHeight() - h);
        shapes.add(new Rectangle(x, y, w, h, Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())));
        redraw();
    }

    @FXML
    private void clearCanvas() {
        shapes.clear();
        redraw();
    }

    @FXML
    private void goBack() throws Exception {
        Main.openMainMenu();



    }
}