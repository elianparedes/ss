package ar.edu.itba.ss;

import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

public class MolecularParams extends AlgorithmParameters {
    private final int n;

    private final int maxIter;

    private final double l;

    private final double mass;

    private final double speed;

    private final double radius;

    public MolecularParams(int n, double l, double mass, double speed, double radius, int maxIter) {
        this.n = n;
        this.l = l;
        this.mass = mass;
        this.speed = speed;
        this.radius = radius;
        this.maxIter = maxIter;
    }

    public int getN() {
        return n;
    }

    public double getL() {
        return l;
    }

    public double getMass() {
        return mass;
    }

    public double getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public int getMaxIter() {
        return maxIter;
    }
}
