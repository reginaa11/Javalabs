package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import app.Main;

public class CalculatorController {

    @FXML private TextField display;

    private StringBuilder current = new StringBuilder();

    @FXML
    private void initialize() {
        display.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);
    }

    @FXML
    private void onDigitClick(javafx.event.ActionEvent e) {
        String text = ((Button) e.getSource()).getText();
        current.append(text);
        display.setText(current.toString());
    }

    @FXML
    private void onOperationClick(javafx.event.ActionEvent e) {
        String op = ((Button) e.getSource()).getText();
        appendOperator(op);
    }

    @FXML
    private void onEqualsClick() {
        try {
            double result = eval(current.toString());
            String resStr = String.valueOf(result);
            display.setText(resStr);
            current.setLength(0);
            current.append(resStr);
        } catch (Exception ex) {
            display.setText("Ошибка");
            current.setLength(0);
        }
    }

    @FXML
    private void onClearClick() {
        current.setLength(0);
        display.setText("");
    }

    @FXML
    private void onBackClick() throws Exception {
        Main.openMainMenu();
    }

    public void handleKeyTyped(KeyEvent e) {
        String c = e.getCharacter();
        if ("0123456789.".contains(c)) {
            current.append(c);
        } else if ("+-*/".contains(c)) {
            appendOperator(c);
        } else if ("\r".equals(c) || "=".equals(c)) {
            onEqualsClick();
        } else if ("\b".equals(c)) {
            if (current.length() > 0) current.setLength(current.length() - 1);
        } else {
            e.consume();
            return;
        }
        display.setText(current.toString());
        e.consume();
    }

    private void appendOperator(String op) {
        if (current.length() == 0) {
            String dispText = display.getText();
            if (!dispText.isEmpty() && !"Ошибка".equals(dispText)) {
                current.append(dispText);
            } else if (op.equals("+") || op.equals("-")) {
                current.append("0");
            } else {
                return;
            }
        }

        char last = current.charAt(current.length() - 1);
        if ("+-*/".indexOf(last) != -1) {
            current.setCharAt(current.length() - 1, op.charAt(0));
        } else {
            current.append(op);
        }
        display.setText(current.toString());
    }

    private double eval(String expr) throws Exception {
        String[] tokens = expr.split("(?<=[-+*/])|(?=[-+*/])");
        if (tokens.length == 0) throw new Exception("Пустое выражение");

        double result = Double.parseDouble(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            String op = tokens[i];
            double val = Double.parseDouble(tokens[i + 1]);
            switch (op) {
                case "+": result += val; break;
                case "-": result -= val; break;
                case "*": result *= val; break;
                case "/":
                    if (val == 0) throw new ArithmeticException("Деление на ноль");
                    result /= val; break;
                default: throw new Exception("Неверный оператор");
            }
        }
        return result;
    }
}