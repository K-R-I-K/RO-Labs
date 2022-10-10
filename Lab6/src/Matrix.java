import java.util.Random;

public final class Matrix {
    public int[] values;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.values = new int[rows * cols];
    }

    public void set(int row, int col, int val) {
        values[row * cols + col] = val;
    }

    public void incr(int row, int col, int val) {
        values[row * cols + col] += val;
    }

    public Matrix multiply(Matrix b){
        Matrix c = new Matrix(this.rows, b.getCols());
        for(int i = 0; i < this.rows; ++i){
            for(int j = 0; j < b.getCols(); ++j){
                for(int k = 0; k < this.rows; ++k){
                    c.incr(i, j, this.get(i, k) * b.get(k, j));
                }
            }
        }
        return c;
    }

    public int get(int row, int col) {
        return values[row * cols + col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int[] getValues() {
        return values;
    }

    public void init() {
        Random random = new Random();
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * 1000);
        }
    }

    public void show() {
        System.out.println("======================================================================================");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(values[i * cols + j] + " ");
            }
            System.out.println();
        }
        System.out.println("======================================================================================");
    }
}
