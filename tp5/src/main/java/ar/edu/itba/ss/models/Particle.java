package ar.edu.itba.ss.models;

import java.util.*;
import java.util.stream.Collectors;

public class Particle {

    private final String name;
    private final double mass;
    private final double radius;
    private double desiredSpeed;
    private  Vector target;
    private final List<Vector> r;

    public Particle(String name, double mass, double radius, Vector position, Vector velocity, double desiredSpeed, Vector target) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.desiredSpeed = desiredSpeed;
        this.target = target;
        this.r = Collections.nCopies(6, 0)
                .stream().map((value) -> new Vector(value, value))
                .collect(Collectors.toCollection(ArrayList::new));
        this.r.set(0, position);
        this.r.set(1, velocity);
    }

    public Particle(String name, double mass, double radius, List<Vector> r, double desiredSpeed, Vector target) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.r = r;
        this.target = target;
        this.desiredSpeed = desiredSpeed;
    }

    public Particle(String name, double mass, double radius) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.r = Collections.nCopies(6, 0)
                .stream().map((value) -> new Vector(value, value))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public Vector getPosition() {
        return r.get(0);
    }

    public Vector setPosition(Vector position) {
        return r.set(0, position);
    }

    public Vector getVelocity() {
        return r.get(1);
    }

    public Vector setVelocity(Vector velocity) {
        return r.set(1, velocity);
    }

    public Vector getAcceleration() {
        return r.get(2);
    }

    public void setAcceleration(Vector acceleration) {
        r.set(2, acceleration);
    }

    public List<Vector> getR() {
        return r;
    }


    public Vector getR(int index) {
        if (index < 0 || index >= r.size()) throw new IndexOutOfBoundsException();

        return r.get(index);
    }

    public Vector setR(int index, Vector value) {
        if (index < 0 || index >= r.size()) throw new IndexOutOfBoundsException();

        return r.set(index, value);
    }

    public double getRadius() {
        return radius;
    }

    public Vector getDesiredSpeedVector() {
        Vector direction = target.sub(this.getPosition());
        direction = direction.divide(direction.norm());
        return direction.multiply(desiredSpeed);
    }

    public double getDesiredSpeed() {
        return desiredSpeed;
    }

    public Vector getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle that = (Particle) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return Arrays.toString(r.toArray());
    }
}
