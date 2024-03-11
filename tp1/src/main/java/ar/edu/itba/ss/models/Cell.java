package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.entity.Entity;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell<T extends Entity> {

    private final int i, j;
    private final List<SurfaceEntity<T>> entities;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
        this.entities = new ArrayList<>();
    }

    public void place(T entity, double x, double y) {
        this.entities.add(new SurfaceEntity<>(entity, x, y));
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public List<SurfaceEntity<T>> getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return String.format("Cell [%d, %d] { %s }", i, j, Arrays.toString(entities.toArray()));
    }
}
