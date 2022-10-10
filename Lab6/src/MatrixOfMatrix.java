import java.util.Arrays;

public class MatrixOfMatrix {
    private Matrix[] values;

    private int rows;
    private int cols;
    private int blockSize;

    public MatrixOfMatrix(final int rows, final int cols, int blockSize) {
        this.rows = rows;
        this.cols = cols;
        this.values = new Matrix[rows * cols];
        this.blockSize = blockSize;
    }

    public void init(Matrix matrix){
        int counter = 0;
        for (int i = 0; i < matrix.getRows() / blockSize; ++i) {
            for (int j = 0; j < matrix.getCols() / blockSize; ++j) {
                Matrix block = new Matrix(blockSize, blockSize);
                for (int i1 = 0; i1 < blockSize; ++i1) {
                    for (int j1 = 0; j1 < blockSize; ++j1) {
                        block.set(i1, j1, matrix.get(i * blockSize + i1, j * blockSize + j1));
                    }
                }
                values[counter] = block;
                ++counter;
            }
        }
    }

    public void set(int row, int col, Matrix val) {
        values[row * cols + col] = val;
    }

    public void incr(int row, int col, Matrix val) {
        for(int i = 0; i < val.values.length; ++i){
            values[row * cols + col].values[i] += val.values[i];
        }
    }

    public Matrix get(int row, int col) {
        return values[row * cols + col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int[] getValues() {
        return toMatrix(this).values;
    }

    public Matrix toMatrix(MatrixOfMatrix matrix){
        Matrix ans = new Matrix(matrix.getRows() * blockSize, matrix.getCols() * blockSize);
        for(int i = 0; i < matrix.getRows(); ++i) {
            for(int j = 0; j < matrix.getCols(); ++j) {
                Matrix tmp = matrix.get(i, j);
                for(int i1 = 0; i1 < tmp.getRows(); ++i1){
                    for(int j1 = 0; j1 < tmp.getCols(); ++j1){
                        ans.set(i * blockSize + i1, j * blockSize + j1, tmp.get(i1, j1));
                    }
                }
            }
        }
        System.out.println(Arrays.toString(ans.values));
        return ans;
    }
}
