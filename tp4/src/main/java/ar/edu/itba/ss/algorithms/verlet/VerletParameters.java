package ar.edu.itba.ss.algorithms.verlet;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

public class VerletParameters extends AlgorithmParameters {

    private final Vector current;

    private final Vector previous;

    private final Vector initialSpeed;

    private final Force force;

    private final double mass;

    private final double dt;

    private final int maxIterations;

    public VerletParameters(Vector current, Vector previous, Vector initialSpeed, Force force, double mass, double dt, int maxIterations) {
        this.current = current;
        this.previous = previous;
        this.initialSpeed = initialSpeed;
        this.force = force;
        this.mass = mass;
        this.dt = dt;
        this.maxIterations = maxIterations;
    }

    public Vector getCurrent() {
        return current;
    }

    public Vector getPrevious() {
        return previous;
    }

    public Force getForce() {
        return force;
    }

    public double getMass() {
        return mass;
    }

    public double getDt() {
        return dt;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public Vector getInitialSpeed() {
        return initialSpeed;
    }
}
