package ar.edu.itba.ss.utils.entity;

import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

public class MovableSurfaceEntity<T extends Entity> extends SurfaceEntity<T> {

    private double speed;

    private double angle;

    public MovableSurfaceEntity(T entity, double x, double y, double speed, double angle) {
        super(entity, x, y);
        this.angle = angle;
        this.speed = speed;
    }

    public static <T extends Entity> MovableSurfaceEntity<T> uniformLinearMotion(MovableSurfaceEntity<T> entity, double t) {
        double x = entity.getX();
        double y = entity.getY();

        double xt = entity.getXSpeed() * t;
        double yt = entity.getYSpeed() * t;

        double newX = x + xt;
        double newY = y + yt;

        return new MovableSurfaceEntity<>(entity.getEntity(), newX, newY, entity.speed, entity.angle);
    }

    public static MovableSurfaceEntity<Particle> collisionMotion(MovableSurfaceEntity<Particle> e1, MovableSurfaceEntity<Particle> e2, double dotProductDvDr, double sigma, double dX, double dY) {


        if(dotProductDvDr >= 0)
            throw new RuntimeException("Should not happend");

        //TODO: Be careful if both particles, in different calls, end up adding Jx and Jy instead of someone subtracting them.
        double J = (2 * e1.getEntity().getMass() * e2.getEntity().getMass() * (dotProductDvDr)) / (sigma * (e1.getEntity().getMass() + e2.getEntity().getMass()));
        double Jx = (J * dX) / sigma;
        double Jy = (J * dY) / sigma;

        double newXSpeed = e1.getXSpeed() + Jx / e1.getEntity().getMass();
        double newYSpeed = e1.getYSpeed() + Jy / e1.getEntity().getMass();

        double newSpeed = Math.sqrt(newXSpeed * newXSpeed + newYSpeed * newYSpeed);
        double newAngle = Math.atan2(newYSpeed,newXSpeed);

        return new MovableSurfaceEntity<>(e1.getEntity(), e1.getX(), e1.getY(), newSpeed, newAngle);
    }

    public static MovableSurfaceEntity<Particle> collisionWithFixed(MovableSurfaceEntity<Particle> p, SurfaceEntity<Border> fixed) {
        double newAngle = Math.atan2(-p.getXSpeed() , p.getYSpeed());
        return new MovableSurfaceEntity<>(p.getEntity(), p.getX(), p.getY(), p.getSpeed(), newAngle);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getXSpeed() {
        return Math.cos(angle) * speed;
    }

    public double getYSpeed() {
        return Math.sin(angle) * speed;
    }

    public void setSpeed(double speedX, double speedY) {
        this.speed = Math.pow(speedX * speedX + speedY * speedY, 2);
    }

    public void setAngle(double speedX, double speedY) {
        this.angle = Math.atan2(speedY , speedX);
    }

    @Override
    public String toString() {
        return String.format("{ x: %.4f, y: %.4f, angle: %.4f, speed: %.4f, entity: %s }", this.getX(), this.getY(), this.angle,
                this.speed, this.getEntity());
    }

    @Override
    public int hashCode() {
        return getEntity().getId();
    }
}
