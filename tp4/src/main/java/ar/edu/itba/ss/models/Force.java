package ar.edu.itba.ss.models;

public interface Force {
    Vector apply(double t);

    Vector apply(Vector v0, Vector v1);
}
