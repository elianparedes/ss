package ar.edu.itba.ss;

public class Collision implements Comparable<Collision>{

    private final double time;

    public Collision(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Collision collision) {
        return Double.compare(this.time,collision.time);
    }

    @Override
    public String toString() {
        return "Collision{" +
                "time=" + time +
                '}';
    }
}
