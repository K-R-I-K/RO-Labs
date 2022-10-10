import mpi.MPI;
import mpi.Request;

public class MatrixMult {

    private MatrixMult() {}
    public static void parallelMatrixMultiply(final MatrixOfMatrix a, final MatrixOfMatrix b, final MatrixOfMatrix c) {
        final int rank = MPI.COMM_WORLD.Rank();
        final int size = MPI.COMM_WORLD.Size();
        final int rows = c.getRows();
        final int cols = c.getCols();
        final int chunk = (rows + size - 1) / size;
        final int startRow = getStartRow(rank, chunk);
        final int endRow = getEndRow(rank, chunk, rows);

        if (startRow < rows) {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < cols; j++) {
                    for (int k = 0; k < b.getRows(); k++) {
                        c.incr(i, j, a.get(i, k).multiply(b.get(k, j)));
                    }
                }
            }

            if (rank == 0) {
                Request[] requests = new Request[size - 1];
                for (int i = 1; i < size; i++) {
                    final int nodeStartRow = getStartRow(i, chunk);
                    if (nodeStartRow >= rows)
                        break;
                    final int nodeEndRow = getEndRow(i, chunk, rows);
                    final int offset = getOffset(nodeStartRow, cols);
                    final int count = getCount(nodeStartRow, nodeEndRow, cols);
                    System.out.println("Request Irec");
                    requests[i - 1] = MPI.COMM_WORLD.Irecv(c.getValues(), offset, count, MPI.INT, i, 77);
                }
                Request.Waitall(requests);
            } else {
                final int offset = getOffset(startRow, cols);
                final int count = getCount(startRow, endRow, cols);
                System.out.println("Request Isend");
                MPI.COMM_WORLD.Isend(c.getValues(), offset, count, MPI.INT, 0, 77);
            }
        }
    }

    private static int getStartRow(final int rank, final int chunk) {
        return rank * chunk;
    }

    private static int getEndRow(final int rank, final int chunk, final int rows) {
        return Math.min((rank + 1) * chunk, rows);
    }

    private static int getOffset(final int startRow, final int cols) {
        return startRow * cols;
    }

    private static int getCount(final int startRow, final int endRow, final int cols) {
        return (endRow - startRow) * cols;
    }
}
