package ar.edu.itba.ss.algorithms.beeman;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.ForceType;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;
import ar.edu.itba.ss.models.Vector;

public class BeemanParameters extends AlgorithmParameters {
    private final double mass;
    private final double dt;
    private final int maxIterations;

    private final Vector current;
    private final Vector initialSpeed;
    private final Vector previousAcceleration;

    private final Vector initialAcceleration;

    private final Force force;

    private final ForceType forceType;


    public BeemanParameters(double mass, double dt, int maxIterations, Vector current, Vector initialSpeed, Vector previousAcceleration, Vector initialAcceleration, Force force, ForceType forceType) {
        this.mass = mass;
        this.dt = dt;
        this.maxIterations = maxIterations;
        this.current = current;
        this.initialSpeed = initialSpeed;
        this.previousAcceleration = previousAcceleration;
        this.initialAcceleration = initialAcceleration;
        this.force = force;
        this.forceType = forceType;
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

    public Vector getCurrent() {
        return current;
    }

    public Vector getInitialSpeed() {
        return initialSpeed;
    }

    public Vector getPreviousAcceleration() {
        return previousAcceleration;
    }

    public Force getForce() {
        return force;
    }

    public ForceType getForceType() {
        return forceType;
    }

    public Vector getInitialAcceleration() {
        return initialAcceleration;
    }
}
