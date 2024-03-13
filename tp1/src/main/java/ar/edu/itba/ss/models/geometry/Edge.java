package ar.edu.itba.ss.models.geometry;

public class Edge implements Geometry {
    private final Point p1;

    private final Point p2;

    private final double slope;

    public Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    }

    @Override
    public boolean belongs(Point p){

        boolean onLine = p.getY() == slope * (p.getX() - p1.getX()) + p1.getY();

        boolean withinBounds = (p.getX() >= Math.min(p1.getX(), p2.getX()) && p.getX() <= Math.max(p1.getX(), p2.getX()))
                && (p.getY() >= Math.min(p1.getY(), p2.getY()) && p.getY() <= Math.max(p1.getY(), p2.getY()));

        return onLine && withinBounds;
    }

    public double distance(Point p){
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double lengthSquared = dx * dx + dy * dy;

        if (lengthSquared == 0.0) {
            return Math.hypot(p.getX() - p1.getX(), p.getY() - p1.getY());
        }

        double t = ((p.getX() - p1.getX()) * dx + (p.getY() - p1.getY()) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));

        double closestX = p1.getX() + t * dx;
        double closestY = p1.getY() + t * dy;

        return Math.hypot(p.getX() - closestX, p.getY() - closestY);
    }
}
