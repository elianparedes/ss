package ar.edu.itba.ss.utils.entity;
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
        this.angle = Math.atan2(speedY, speedX);
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
