package Striped;

public class Matrix {

    private final int[] values;
    private final int size;

    public Matrix(int size) {
        this.size = size;
        this.values = new int[size * size];
    }

    public Matrix(int[] values) {
        this.size = (int) Math.sqrt(values.length);
        this.values = values;
    }


    public void increment(int row, int col, int val) {
        values[row * size + col] += val;
    }

    public int get(int row, int col) {
        return values[row * size + col];
    }

    public int getSize() {
        return size;
    }


    public int[] getValues() {
        return values;
    }

    public void init() {
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