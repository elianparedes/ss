package ar.edu.itba.ss.cim.config;

public class TraversalOffset {

    public static final int[][] EIGHT_NEIGHBOURS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 0}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    public static final int[][] L_NEIGHBOURS = {
            {0, 1},
            {1, 1},
            {1, 0},
            {1, -1},
            {0, 0}
    };

}

