package Striped;

import mpi.MPI;

import java.util.Arrays;

public class Striped {
    public static final int MATRIX_SIZE = 4;

    public static Matrix c;

    public static void multiply(Matrix a, Matrix b, Matrix tmpC, int step) {
        int size = MPI.COMM_WORLD.Size();
        if(step < MATRIX_SIZE) {
            for (int i = 0, j = step; i < MATRIX_SIZE; ++i, ++j) {
                if (j >= MATRIX_SIZE) {
                    j = 0;
                }
                for (int k = 0; k < MATRIX_SIZE; ++k) {
                    tmpC.increment(i, j, a.get(i, k) * b.get(k, j));
                }
            }
        }

        int[] result = new int[MATRIX_SIZE * MATRIX_SIZE * size];

        MPI.COMM_WORLD.Gather(
                tmpC.getValues(),
                0,
                MATRIX_SIZE * MATRIX_SIZE,
                MPI.INT,
                result,
                0,
                MATRIX_SIZE * MATRIX_SIZE,
                MPI.INT,
                0
        );
        if (step >= MATRIX_SIZE - MPI.COMM_WORLD.Size()) {
            c = convertToMatrix(result);
        }
    }

    private static Matrix convertToMatrix(int[] result) {
        int size = MPI.COMM_WORLD.Size();
        int[][] arr = new int[size][MATRIX_SIZE * MATRIX_SIZE];
        for(int i = 0; i < size; ++i) {
            arr[i] = Arrays.copyOfRange(result, i * result.length / size, (i + 1) * result.length / size);
        }
        int[] array = new int[MATRIX_SIZE * MATRIX_SIZE];
        for(int i = 0; i < MATRIX_SIZE * MATRIX_SIZE; ++i) {
            for(int j = 0; j < size; ++j) {
                array[i] = Math.max(array[i], arr[j][i]);
            }
        }
        return new Matrix(array);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        Matrix a = new Matrix(MATRIX_SIZE);
        Matrix b = new Matrix(MATRIX_SIZE);
        Matrix tmpC = new Matrix(MATRIX_SIZE);
        if (rank == 0) {
            a.init();
            b.init();
            a.show();
            b.show();
        }
        MPI.COMM_WORLD.Barrier();
        MPI.COMM_WORLD.Bcast(a.getValues(), 0, a.getValues().length, MPI.INT, 0);
        MPI.COMM_WORLD.Bcast(b.getValues(), 0, b.getValues().length, MPI.INT, 0);
        long start = System.currentTimeMillis();
        for(int i = 0; i < MATRIX_SIZE / MPI.COMM_WORLD.Size() + 1; ++i) {
            multiply(a, b, tmpC, rank + MPI.COMM_WORLD.Size() * i);
        }
        if (MPI.COMM_WORLD.Rank() == 0) {
            long stop = System.currentTimeMillis();
            System.out.println(stop - start + "ms");
            c.show();
        }
        MPI.Finalize();
    }
}