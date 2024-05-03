package ar.edu.itba.ss.algorithms.gear;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

import java.util.ArrayList;
import java.util.List;

public class GearParameters extends AlgorithmParameters {

    private final Vector initialSpeed;
    private final Vector initialAcceleration;
    private final Vector initialPosition;

    private final Force force;

    private final double mass;

    private final double dt;

    private final List<Vector> r;
    private final double[] alpha;
    private final int maxIterations;

    public GearParameters(Vector initialSpeed, Vector initialAcceleration, Vector initialPosition, double[] alpha, Force force, double mass, double dt, int maxIterations) {
        this.initialSpeed = initialSpeed;
        this.initialAcceleration = initialAcceleration;
        this.initialPosition = initialPosition;
        this.force = force;
        this.mass = mass;
        this.dt = dt;
        this.maxIterations = maxIterations;
        this.alpha = alpha;

        this.r = new ArrayList<>();
        r.add(0, initialPosition);
        r.add(1, initialSpeed);
        r.add(2, initialAcceleration);
        r.add(3, force.apply(this.r.get(1), this.r.get(2)).divide(mass));
        r.add(4, force.apply(this.r.get(2), this.r.get(3)).divide(mass));
        r.add(5, force.apply(this.r.get(3), this.r.get(4)).divide(mass));
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

    public Vector getInitialAcceleration() {
        return initialAcceleration;
    }

    public Vector getInitialPosition() {
        return initialPosition;
    }

    public List<Vector> getR() {
        return r;
    }

    public double[] getAlpha() {
        return alpha;
    }
}
