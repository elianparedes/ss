package ar.edu.itba.ss.dynamics;

import ar.edu.itba.ss.models.Vector;

public class VerletState {
    private final Vector position;
    private final Vector speed;

    public VerletState(Vector position, Vector speed) {
        this.position = position;
        this.speed = speed;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }
}
