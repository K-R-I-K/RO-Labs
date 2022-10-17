package Fox;

import mpi.Cartcomm;
import mpi.MPI;

public class Grid {
    int processCount;
    int dimensions;
    int row;
    int col;
    int gridRank;
    int worldRank;
    Cartcomm gridComm;
    Cartcomm rowComm;
    Cartcomm colComm;

    public Grid() {
        int[] dimensions = new int[2];
        boolean[] wrap_around = new boolean[2];
        int[] coordinates;
        boolean[] freeCoords = new boolean[2];
        this.processCount = MPI.COMM_WORLD.Size();
        this.worldRank = MPI.COMM_WORLD.Rank();

        this.dimensions = (int) Math.sqrt(this.processCount);
        dimensions[0] = this.dimensions;
        dimensions[1] = this.dimensions;

        wrap_around[0] = false;
        wrap_around[1] = true;

        this.gridComm = MPI.COMM_WORLD.Create_cart(dimensions, wrap_around, true);
        this.gridRank = this.gridComm.Rank();
        coordinates = this.gridComm.Coords(this.gridRank);
        this.row = coordinates[0];
        this.col = coordinates[1];

        freeCoords[0] = false;
        freeCoords[1] = true;
        this.rowComm = this.gridComm.Sub(freeCoords);

        freeCoords[0] = true;
        freeCoords[1] = false;
        this.colComm = this.gridComm.Sub(freeCoords);
    }
}
