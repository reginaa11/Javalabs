package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.CalculatorController;
import java.io.IOException;

public class Main extends Application {

    private static final String STYLE_PATH = "/style.css";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        openMainMenu();
    }

    public static void openMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource(STYLE_PATH).toExternalForm());
        primaryStage.setTitle("Выбор задания");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void openCalculator() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Calculator.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource(STYLE_PATH).toExternalForm());

        CalculatorController controller = loader.getController();
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, controller::handleKeyTyped);

        primaryStage.setTitle("Калькулятор");
        primaryStage.setScene(scene);
    }

    public static void openShapes() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Shapes.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource(STYLE_PATH).toExternalForm());

        primaryStage.setTitle("Геометрические фигуры");
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}