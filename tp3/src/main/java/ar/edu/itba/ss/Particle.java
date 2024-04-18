package ar.edu.itba.ss;

import java.util.concurrent.atomic.AtomicInteger;

public class Particle {

    private final static AtomicInteger atomicInteger = new AtomicInteger(1);

    private final Integer id;

    private final double x;

    private final double y;

    private final double Vx;

    private final double Vy;

    private final double radius;

    private final double mass;

    public Particle(double x, double y, double vx, double vy, double radius, double mass) {
        this.x = x;
        this.y = y;
        this.Vx = vx;
        this.Vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.id = atomicInteger.getAndIncrement();
    }

    public Particle(Integer id, double x, double y, double vx, double vy, double radius, double mass) {
        this.id = id;
        this.x = x;
        this.y = y;
        Vx = vx;
        Vy = vy;
        this.radius = radius;
        this.mass = mass;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return Vx;
    }

    public double getVy() {
        return Vy;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Integer getId() {
        return id;
    }

    public double getSpeedAngle(){
        return Math.atan2(Vy,Vx);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(!(obj instanceof Particle))
            return false;

        Particle other = (Particle) obj;
        return other.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("{id: %s , x: %s, y: %s}",id,x,y);
    }
}
