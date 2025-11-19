import java.util.Iterator;

public class PrimesGenerator implements Iterable<Integer> {
    private int count;

    public PrimesGenerator(int count) {
        this.count = count;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = 2;
            private int found = 0;

            @Override
            public boolean hasNext() {
                return found < count;
            }

            @Override
            public Integer next() {
                while (!isPrime(current)) {
                    current++;
                }
                found++;
                return current++;
            }
        };
    }
}