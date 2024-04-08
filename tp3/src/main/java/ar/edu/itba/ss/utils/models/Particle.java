package ar.edu.itba.ss.utils.models;

import ar.edu.itba.ss.utils.entity.Entity;

public class Particle extends Entity {

    private final double radius;

    private final double mass;

    public Particle() {
        this.radius = 0;
        this.mass = 0;
    }

    public Particle(double radius, double mass) {
        this.radius = radius;
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    @Override
    public String toString() {
        return String.format("{id: %s, radius: %s}", getId(), getRadius());

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

