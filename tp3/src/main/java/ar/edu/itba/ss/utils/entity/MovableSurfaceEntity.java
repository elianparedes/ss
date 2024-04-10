package ar.edu.itba.ss.utils.entity;

import ar.edu.itba.ss.utils.models.Particle;

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

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSpeed(double speedX, double speedY) {
        this.speed = Math.pow(speedX*speedX + speedY*speedY,2);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setAngle(double speedX, double speedY) {
        this.angle = Math.tan(speedY/speedX);
    }

    public static <T extends Entity> MovableSurfaceEntity<T> uniformLinearMotion(MovableSurfaceEntity<T> entity, double t){
        double x = entity.getX();
        double y = entity.getY();

        double xt = entity.getXSpeed()*t;
        double yt = entity.getYSpeed()*t;

        double newX = x + xt;
        double newY = y + yt;

        return new MovableSurfaceEntity<>(entity.getEntity(), newX, newY, entity.speed,entity.angle);
    }

    public static MovableSurfaceEntity<Particle> collisionMotion(MovableSurfaceEntity<Particle> e1, MovableSurfaceEntity<Particle> e2){

        double sigma = e1.getEntity().getRadius() + e2.getEntity().getRadius();
        double[] dR = new double[]{e2.getX() - e1.getX(), e2.getY() - e1.getY()};
        double[] dV = new double[]{e2.getXSpeed() - e1.getXSpeed(), e2.getYSpeed() - e1.getYSpeed()};

        double dotProductDvDr = dR[0] * dV[0] + dR[1] * dV[1];

        //TODO: Be careful if both particles, in different calls, end up adding Jx and Jy instead of someone subtracting them.
        double J = ( 2*e1.getEntity().getMass()*e2.getEntity().getMass()*(dotProductDvDr) ) / ( sigma*(e1.getEntity().getMass() + e2.getEntity().getMass()) );
        double Jx = ( J*(e2.getX()-e1.getX()) )/sigma;
        double Jy = ( J*(e2.getY()-e1.getY()) )/sigma;

        double newXspeed = e1.getXSpeed() + Jx/e1.getEntity().getMass();
        double newYspeed = e1.getYSpeed() + Jy/e1.getEntity().getMass();

        double newSpeed = Math.sqrt(newXspeed*newXspeed + newYspeed*newYspeed);
        double newAngle = Math.tan(newYspeed/newXspeed);

        return new MovableSurfaceEntity<>(e1.getEntity(),e1.getX(),e1.getY(),newSpeed,newAngle);

    }

    @Override
    public String toString() {
        return String.format("{ x: %.2f, y: %.2f, angle: %.2f, speed: %.2f, entity: %s }", this.getX(), this.getY(), this.angle,
                this.speed,this.getEntity());
    }
}
