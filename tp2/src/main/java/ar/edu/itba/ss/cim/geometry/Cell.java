package ar.edu.itba.ss.cim.geometry;

import ar.edu.itba.ss.cim.entity.Entity;
import ar.edu.itba.ss.cim.entity.SurfaceEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cell<T extends Entity> {

    private final int i, j;

    private final double size;
    private final List<SurfaceEntity<T>> entities;

    public Cell(int i, int j, double size) {
        this.i = i;
        this.j = j;
        this.size = size;
        this.entities = new ArrayList<>();
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

