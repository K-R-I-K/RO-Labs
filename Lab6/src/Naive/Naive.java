package Naive;

public class Naive {
    private static final int MATRIX_SIZE = 4;

    public static void main(String[] args) {
        Matrix a = new Matrix(MATRIX_SIZE);
        Matrix b = new Matrix(MATRIX_SIZE);
        a.initialise();
        b.initialise();
        a.show();
        b.show();
        long start = System.currentTimeMillis();
        Matrix c = a.multiplyMatrix(b, MATRIX_SIZE);
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start) + "ms");
        c.show();
    }
}
