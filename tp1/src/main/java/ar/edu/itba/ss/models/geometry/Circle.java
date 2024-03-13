package ar.edu.itba.ss.models.geometry;

public class Circle implements Geometry{

    private final double radius;

    private final Point center;

    public Circle(double radius, Point center) {
        this.radius = radius;
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public Point getCenter() {
        return center;
    }

    @Override
    public boolean belongs(Point point) {
        return (Math.pow(point.getX()-center.getX(),2) + Math.pow(point.getY()-center.getY(),2)) <= radius;
    }
}
