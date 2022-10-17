package Naive;

public class Matrix {
    private final int[] values;
    private final int size;

    public Matrix(int size) {
        this.size = size;
        this.values = new int[size * size];
    }

    public int get(int row, int col) {
        return values[row * this.size + col];
    }

    public int getSize() {
        return this.size;
    }

    public Matrix multiplyMatrix(Matrix b, int size){
        Matrix c = new Matrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    c.increment(i, j, this.get(i, k) * b.get(k, j));
                }
            }
        }
        return c;
    }

    public void increment(int row, int col, int val) {
        values[row * size + col] += val;
    }

    public void incrementMatrix(Matrix val){
        for(int i = 0; i < val.getSize(); ++i){
            for(int j = 0; j < val.getSize(); ++j){
                this.increment(i,  j, val.get(i, j));
            }
        }
    }

    public void initialise() {
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * 10);
        }
    }

    public void show() {
        System.out.println("======================================================================================");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(values[i * size + j] + " ");
            }
            System.out.println();
        }
        System.out.println("======================================================================================");
    }
}
