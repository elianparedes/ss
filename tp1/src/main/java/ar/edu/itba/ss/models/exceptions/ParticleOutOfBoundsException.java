package ar.edu.itba.ss.models.exceptions;

public class ParticleOutOfBoundsException extends RuntimeException{

    private final static String MESSAGE = "The given particle coordinates are out of bounds";
    public ParticleOutOfBoundsException() {
        super(MESSAGE);
    }
}
