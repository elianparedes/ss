package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Vector;

public class AlgorithmState {
    private final Vector position;

    private final double time;

    private final double dt;

    public AlgorithmState(Vector position, double time, double dt) {
        this.position = position;
        this.time = time;
        this.dt = dt;
    }

    public Vector getPosition() {
        return position;
    }


    public double getTime() {return time;}

    public double getDt() {
        return dt;
    }
}
