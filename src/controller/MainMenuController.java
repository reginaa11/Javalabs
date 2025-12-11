package controller;

import javafx.fxml.FXML;

import app.Main;

public class MainMenuController {

    @FXML
    private void openCalculator() throws Exception {
        Main.openCalculator();
    }

    @FXML
    private void openShapes() throws Exception {
        Main.openShapes();
    }

    @FXML
    private void exitApp() {
        System.exit(0);
    }

}