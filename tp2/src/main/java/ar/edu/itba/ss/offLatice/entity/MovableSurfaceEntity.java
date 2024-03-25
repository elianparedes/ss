package ar.edu.itba.ss.offLatice.entity;

import ar.edu.itba.ss.cim.entity.Entity;
import ar.edu.itba.ss.cim.entity.SurfaceEntity;

public class MovableSurfaceEntity<T extends Entity> extends SurfaceEntity<T> {

    private double speed;

    private double angle;
    public MovableSurfaceEntity(T entity, double x, double y, double speed, double angle) {
        super(entity, x, y);
        this.angle = angle;
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public double getXSpeed(){
        return Math.cos(angle)*speed;
    }

    public double getYSpeed(){
        return Math.sin(angle)*speed;
    }

    @Override
    public String toString() {
        return String.format("{ x: %.2f, y: %.2f, angle: %.2f, speed: %.2f, entity: %s }", this.getX(), this.getY(), this.angle,
                this.speed,this.getEntity());
    }
}
