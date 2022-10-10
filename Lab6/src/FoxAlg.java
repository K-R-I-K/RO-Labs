import mpi.MPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FoxAlg {
    private static int rows = 4;
    private static int cols = 4;
    private static int blockSize = 2;

    public static void main(String[] args) {
        //Specify the location of txt file to write the logs
        String pathToLogFile = "";
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        long start = 0;
        MatrixOfMatrix a = new MatrixOfMatrix(rows / blockSize, cols / blockSize, blockSize);
        MatrixOfMatrix b = new MatrixOfMatrix(rows / blockSize, cols / blockSize, blockSize);
        MatrixOfMatrix c = new MatrixOfMatrix(rows / blockSize, cols / blockSize, blockSize);
        if (rank == 0) {
            Matrix matrixA = new Matrix(rows, cols);
            Matrix matrixB = new Matrix(rows, cols);
            matrixA.init();
            matrixB.init();
            a.init(matrixA);
            b.init(matrixB);
        }
        MPI.COMM_WORLD.Barrier();
        System.out.println("Bcast A");
        MPI.COMM_WORLD.Bcast(a.getValues(), 0, a.getValues().length, MPI.INT, 0);
        System.out.println("Bcast B");
        MPI.COMM_WORLD.Bcast(b.getValues(), 0, b.getValues().length, MPI.INT, 0);
        if (rank == 0) {
            start = System.currentTimeMillis();
        }
        MatrixMult.parallelMatrixMultiply(a, b, c);
        MPI.COMM_WORLD.Barrier();
        if (rank == 0) {
            long stop = System.currentTimeMillis();
            try {
                Files.writeString(Path.of(pathToLogFile), stop-start + "\n", StandardOpenOption.APPEND);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            a.toMatrix(a).show();
            b.toMatrix(b).show();
            c.toMatrix(c).show();
        }
        MPI.Finalize();
    }
}
