package calculator;

public class Calculator {

    private double accumulator = 0;
    private Operation currentOperation = Operation.NONE;

    public void setOperation(Operation op) {
        this.currentOperation = op;
    }

    public double getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(double value) {
        this.accumulator = value;
    }

    public double compute(double nextValue) {
        switch (currentOperation) {
            case ADD:
                accumulator += nextValue;
                break;
            case SUBTRACT:
                accumulator -= nextValue;
                break;
            case MULTIPLY:
                accumulator *= nextValue;
                break;
            case DIVIDE:
                if (nextValue == 0)
                    throw new ArithmeticException("Division by zero");
                accumulator /= nextValue;
                break;
            case NONE:
                accumulator = nextValue;
                break;
        }
        return accumulator;
    }

    public void clear() {
        accumulator = 0;
        currentOperation = Operation.NONE;
    }
}