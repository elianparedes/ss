package ar.edu.itba.ss.algorithms.verlet;

import ar.edu.itba.ss.models.Vector;

public class VerletState {
    private final Vector position;
    private final Vector speed;

    private final double time;

    private final double dt;

    public VerletState(Vector position, Vector speed, double time, double dt) {
        this.position = position;
        this.speed = speed;
        this.time = time;
        this.dt = dt;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }

    public double getTime() {return time;}

    public double getDt() {
        return dt;
    }
}
