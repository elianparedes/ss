package ar.edu.itba.ss.models;

public class Vector {
    private final double x;

    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector multiply(double scalar){
        return new Vector(x*scalar,y*scalar);
    }

    public Vector sum(Vector vector){
        return new Vector(x + vector.x , y + vector.y);
    }

    public Vector sub(Vector vector){
        return new Vector(x - vector.x, y - vector.y);
    }

    @Override
    public String toString() {
        return String.format("( %f ; %f)", x, y);
    }
}
