package Cannon;

import mpi.MPI;

import static java.lang.Math.sqrt;

public class Cannon {
    private static int[][] a;
    private static int[][] b;
    private static int[][] c;
    private static int[] blockA;
    private static int[] blockB;
    private static int[] blockC;
    private static int[] tmpA;
    private static int[] tmpB;
    private static int sizeOfBlock1;
    private static int sizeOfBlock2;
    private static int size;
    private static int numberOfBlocksInRow;
    private static final int MATRIX_SIZE = 4;

    private static int getIndex(int row, int col, int sqrtNum) {
        return ((row + sqrtNum) % sqrtNum) * sqrtNum + (col + sqrtNum) % sqrtNum;
    }

    private static void randomAB() {
        for(int i = 0; i < MATRIX_SIZE; ++i) {
            for (int j = 0; j < MATRIX_SIZE; ++j) {
                a[i][j] = (int) (Math.random() * 10);
                b[i][j] = (int) (Math.random() * 10);
            }
        }
    }

    private static void scatterAB() {
        int l;
        int pIMin;
        int pIMax;
        int pJMin;
        int pJMax;
        for(int k = 0; k< size; ++k) {
            pJMin = (k % numberOfBlocksInRow) * sizeOfBlock1;
            pJMax = (k % numberOfBlocksInRow + 1) * sizeOfBlock1 - 1;
            pIMin = (k - (k % numberOfBlocksInRow))/ numberOfBlocksInRow * sizeOfBlock1;
            pIMax = ((k - (k % numberOfBlocksInRow))/ numberOfBlocksInRow + 1) * sizeOfBlock1 - 1;
            l = 0;

            for(int i=pIMin; i<=pIMax; ++i) {
                for(int j=pJMin; j<=pJMax; ++j) {
                    tmpA[l] = a[i][j];
                    tmpB[l] = b[i][j];
                    ++l;
                }
            }

            if(k==0) {
                System.arraycopy(tmpA, 0, blockA, 0, sizeOfBlock2);
                System.arraycopy(tmpB, 0, blockB, 0, sizeOfBlock2);
            } else {
                MPI.COMM_WORLD.Send(tmpA, 0, sizeOfBlock2, MPI.INT, k, 1);
                MPI.COMM_WORLD.Send(tmpB,0, sizeOfBlock2, MPI.INT, k, 2);
            }
        }
    }

    private static void initialiseTmpBlocks(int myRow, int myCol) {
        MPI.COMM_WORLD.Sendrecv(blockA, 0, sizeOfBlock2, MPI.INT, getIndex(myRow,myCol-myRow, numberOfBlocksInRow), 1,
                tmpA, 0, sizeOfBlock2, MPI.INT, getIndex(myRow,myCol+myRow, numberOfBlocksInRow), 1);
        System.arraycopy(tmpA, 0, blockA, 0, sizeOfBlock2);

        MPI.COMM_WORLD.Sendrecv(blockB, 0, sizeOfBlock2, MPI.INT, getIndex(myRow-myCol,myCol, numberOfBlocksInRow), 1,
                tmpB, 0, sizeOfBlock2, MPI.INT, getIndex(myRow+myCol,myCol, numberOfBlocksInRow), 1);
        System.arraycopy(tmpB,0, blockB,0, sizeOfBlock2);
    }

    private static void shiftBlocks(int myRow, int myCol) {
        for(int l = 0; l < numberOfBlocksInRow; ++l) {
            for(int i = 0; i < sizeOfBlock1; ++i) {
                for (int j = 0; j < sizeOfBlock1; ++j) {
                    for (int k = 0; k < sizeOfBlock1; ++k) {
                        blockC[i * sizeOfBlock1 + j] += blockA[i * sizeOfBlock1 + k] * blockB[k * sizeOfBlock1 + j];
                    }
                }
            }
            MPI.COMM_WORLD.Sendrecv_replace(blockA, 0, sizeOfBlock2, MPI.INT, getIndex(myRow, myCol-1, numberOfBlocksInRow),
                    1,getIndex(myRow, myCol + 1, numberOfBlocksInRow),1);
            MPI.COMM_WORLD.Sendrecv_replace(blockB, 0, sizeOfBlock2, MPI.INT, getIndex(myRow-1, myCol, numberOfBlocksInRow),
                    1,getIndex(myRow+1, myCol, numberOfBlocksInRow),1);

        }
    }

    private static void getAnswer() {
        int i2;
        int j2;
        int pIMin;
        int pIMax;
        int pJMin;
        int pJMax;

        for (int i = 0; i < sizeOfBlock1; ++i) {
            System.arraycopy(blockC, i * sizeOfBlock1, c[i], 0, sizeOfBlock1);
        }

        for (int k = 1; k < size; ++k) {
            MPI.COMM_WORLD.Recv(blockC, 0, sizeOfBlock2, MPI.INT, k, 1);
            pJMin = (k % numberOfBlocksInRow) * sizeOfBlock1;
            pJMax = (k % numberOfBlocksInRow + 1) * sizeOfBlock1 - 1;
            pIMin = (k - (k % numberOfBlocksInRow)) / numberOfBlocksInRow * sizeOfBlock1;
            pIMax = ((k - (k % numberOfBlocksInRow)) / numberOfBlocksInRow + 1) * sizeOfBlock1 - 1;
            i2=0;
            for(int i=pIMin; i<=pIMax; ++i) {
                j2=0;
                for(int j=pJMin; j<=pJMax; ++j) {
                    c[i][j] = blockC[i2* sizeOfBlock1 +j2];
                    j2++;
                }
                i2++;
            }
        }
    }

    private static void print(int[][] m) {
        System.out.println("======================================================================================");
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("======================================================================================");
    }

    public static void main(String[] args) {
        MPI.Init(args);
        size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        numberOfBlocksInRow = (int) sqrt(size);
        sizeOfBlock1 = MATRIX_SIZE / numberOfBlocksInRow;
        sizeOfBlock2 = sizeOfBlock1 * sizeOfBlock1;
        int col =  rank % numberOfBlocksInRow;
        int row = (rank - col) / numberOfBlocksInRow;

        blockA = new int[sizeOfBlock2];
        blockB = new int[sizeOfBlock2];
        blockC = new int[sizeOfBlock2];
        tmpA = new int[sizeOfBlock2];
        tmpB = new int[sizeOfBlock2];

        long start = 0;
        if (rank == 0) {
            a = new int[MATRIX_SIZE][MATRIX_SIZE];
            b = new int[MATRIX_SIZE][MATRIX_SIZE];
            c = new int[MATRIX_SIZE][MATRIX_SIZE];
            randomAB();
            print(a);
            print(b);
            start = System.currentTimeMillis();
            scatterAB();
        } else {
            MPI.COMM_WORLD.Recv(blockA,0, sizeOfBlock2, MPI.INT, 0 , 1);
            MPI.COMM_WORLD.Recv(blockB, 0, sizeOfBlock2, MPI.INT, 0 , 2);
        }

        initialiseTmpBlocks(row, col);

        shiftBlocks(row, col);

        if(rank == 0) {
            getAnswer();
        } else {
            MPI.COMM_WORLD.Send(blockC, 0, sizeOfBlock2, MPI.INT, 0, 1);
        }

        MPI.COMM_WORLD.Barrier();
        if(rank == 0){
            long stop = System.currentTimeMillis();
            System.out.println("Time: " + (stop - start) + "ms");
        }
        if(rank == 0) {
            print(c);
        }
        MPI.Finalize();
    }
}

