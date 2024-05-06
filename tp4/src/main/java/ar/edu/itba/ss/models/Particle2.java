package ar.edu.itba.ss.models;

public class Particle2 {

    private final Vector position;

    private final Vector speed;

    private Vector acceleration;

    private final double radius;

    private final double mass;

    public Particle2(Vector position, Vector speed, Vector acceleration, double radius, double mass) {
        this.position = position;
        this.speed = speed;
        this.acceleration = acceleration;
        this.radius = radius;
        this.mass = mass;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }
}
