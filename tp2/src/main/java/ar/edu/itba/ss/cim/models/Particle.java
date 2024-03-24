package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.entity.Entity;

public class Particle extends Entity {

    private double radius;

    public Particle() {
        this.radius = 0;
    }

    public Particle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
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

