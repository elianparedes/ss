package ar.edu.itba.ss.utils.models;

import ar.edu.itba.ss.utils.entity.Entity;

public class Ball extends Entity {

    private final double radius;
    private final double mass;

    public Ball(double radius, double mass) {
        this.radius = radius;
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }
}
