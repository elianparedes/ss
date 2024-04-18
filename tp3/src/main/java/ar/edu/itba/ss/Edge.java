package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.Point;

public class Edge {
    private final Point p1;

    private final Point p2;

    public Edge(Point p1, Point p2) {
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
        return String.format("Edge: { %s , %s }",p1,p2);
    }
}
