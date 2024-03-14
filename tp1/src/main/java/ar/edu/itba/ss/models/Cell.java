package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.entity.Entity;
import ar.edu.itba.ss.models.entity.SurfaceEntity;
import ar.edu.itba.ss.models.geometry.Circle;
import ar.edu.itba.ss.models.geometry.Edge;
import ar.edu.itba.ss.models.geometry.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cell<T extends Entity> {

    private final int i, j;

    private final double size;
    private final Point top_left, top_right, bottom_left, bottom_right;
    private final Edge left, right, top, bottom;

    private final List<SurfaceEntity<T>> entities;

    public Cell(int i, int j, double size) {
        this.i = i;
        this.j = j;
        this.size = size;
        this.entities = new ArrayList<>();

        this.top_left = new Point(j*size,i*size);
        this.bottom_left = new Point(j*size,(i+1)*size);
        this.top_right = new Point((j+1)*size,i*size);
        this.bottom_right = new Point((j+1)*size,(i+1)*size);

        this.left = new Edge(top_left, bottom_left);
        this.right = new Edge(top_right,bottom_right);
        this.top = new Edge(top_left, top_right);
        this.bottom = new Edge(bottom_left, bottom_right);
    }

    public void place(T entity, double x, double y) {
        this.entities.add(new SurfaceEntity<>(entity, x, y));
    }

    public void place(SurfaceEntity<T> surfaceEntity) {
        this.entities.add(surfaceEntity);
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public double getCenterX() {
        return j * size + size / 2;
    }

    public double getCenterY() {
        return i * size + size / 2;
    }

    public double getSize() {
        return size;
    }

    public boolean intersectCircle(double x, double y, double r){
        Circle circle = new Circle(r,new Point(x,y));
        return top.distance(circle.getCenter()) <= r || bottom.distance(circle.getCenter()) <= r
                || left.distance(circle.getCenter()) <= r || right.distance(circle.getCenter()) <=r;
    }

    public List<SurfaceEntity<T>> getEntities() {
        return entities;
    }


    @Override
    public String toString() {
        return String.format("Cell [%d, %d] { %s }", i, j, Arrays.toString(entities.toArray()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, size, entities);
    }
}
