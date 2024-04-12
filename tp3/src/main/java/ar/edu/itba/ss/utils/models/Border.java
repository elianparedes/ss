package ar.edu.itba.ss.utils.models;

import ar.edu.itba.ss.utils.entity.Entity;
import ar.edu.itba.ss.utils.geometry.Point;

public class Border extends Entity {
    private final Point p1;
    private final Point p2;


    public Border(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    @Override
    public String toString() {
        return String.format("{ p1: %s, p2: %s }", p1, p2);
    }
}
