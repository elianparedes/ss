package ar.edu.itba.ss.models.entity;

import java.util.Objects;

public class SurfaceEntity<T extends Entity> {
    private final T entity;
    private double x;
    private double y;

    public SurfaceEntity(T entity, double x, double y) {
        super();
        this.entity = entity;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public T getEntity() {
        return entity;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distanceTo(SurfaceEntity<T> e) {
        double x = e.getX();
        double y = e.getY();

        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    @Override
    public String toString() {
        return String.format("{ x: %.2f, y: %.2f, entity: %s }", x, y, entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SurfaceEntity<?> that = (SurfaceEntity<?>) obj;
        return entity.equals(that.entity);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
