package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class EdgeCollision extends Collision{

    private final Particle p;

    private final Edge edge;

    public EdgeCollision(double time, Particle p, Edge edge) {
        super(time);
        this.p = p;
        this.edge = edge;
    }

    public static EdgeCollision getCollision(Particle p, Edge edge){
        if (edge.getP1().getX() == edge.getP2().getX()) {
            double edgeX = edge.getP1().getX();
            double sign = Math.signum(edgeX - p.getX());
            double targetX = edgeX - sign * p.getRadius();
            if (sign * p.getVx() > 0) {
                double time = (targetX - p.getX()) / p.getVx();
                return new EdgeCollision(time, p, edge);
            } else {
                return new EdgeCollision(Double.MAX_VALUE, p, edge);
            }
        }

        if (edge.getP1().getY() == edge.getP2().getY()) {
            double edgeY = edge.getP1().getY();
            double sign = Math.signum(edgeY - p.getY());
            double targetY = edgeY - sign * p.getRadius();
            if (sign * p.getVy() > 0) {
                double time = (targetY - p.getY()) / p.getVy();
                return new EdgeCollision(time, p, edge);
            } else {
                return new EdgeCollision(Double.MAX_VALUE, p, edge);
            }
        }

        throw new RuntimeException("Method not supported for non-orthogonal edges");
    }


    public Particle getP() {
        return p;
    }

    public Edge getEdge() {
        return edge;
    }

    @Override
    public String toString() {
        return String.format("Collision: {time: %s, p: %s, edge: %s}",getTime(),p,edge);
    }

    @Override
    public int compareTo(Collision collision) {
        if(!(collision instanceof EdgeCollision)) return super.compareTo(collision);

        EdgeCollision edgeCollision = (EdgeCollision) collision;

        int compareTime = super.compareTo(collision);
        if(compareTime == 0){
            int idCompare = Integer.compare(p.getId(),edgeCollision.getP().getId());
            if(idCompare == 0){
                int xCompare = Double.compare(Math.min(edge.getP1().getX(),edge.getP2().getX()),
                        Math.min(edgeCollision.getEdge().getP1().getX(),edgeCollision.getEdge().getP2().getX()));
                if(xCompare == 0){
                    return Double.compare(Math.min(edge.getP1().getY(),edge.getP2().getY()),
                            Math.min(edgeCollision.getEdge().getP1().getY(),edgeCollision.getEdge().getP2().getY()));
                } else return xCompare;

            } else return idCompare;

        } else return compareTime;
    }
}
