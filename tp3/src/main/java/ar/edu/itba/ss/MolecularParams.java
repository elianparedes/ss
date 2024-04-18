package ar.edu.itba.ss;

import ar.edu.itba.ss.simulation.algorithms.AlgorithmParameters;

public class MolecularParams extends AlgorithmParameters {
    private final int n;

    private final double maxIter;

    private final double l;

    private final double mass;

    private final double speed;

    private final double radius;

    private final Particle fixedParticle;

    private final double Cn;

    private final double Ct;

    public MolecularParams(int n, double l, double mass, double speed, double radius, double maxIter, Particle fixedParticle, double cn, double ct) {
        this.n = n;
        this.l = l;
        this.mass = mass;
        this.speed = speed;
        this.radius = radius;
        this.maxIter = maxIter;
        this.fixedParticle = fixedParticle;
        Cn = cn;
        Ct = ct;
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

    public double getMaxIter() {
        return maxIter;
    }

    public Particle getFixedParticle() {
        return fixedParticle;
    }

    public double getCn() {
        return Cn;
    }

    public double getCt() {
        return Ct;
    }
}
