package ar.edu.itba.ss;

import ar.edu.itba.ss.dynamics.VerletAlgorithm;
import ar.edu.itba.ss.dynamics.VerletParameters;
import ar.edu.itba.ss.dynamics.VerletState;
import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

public class Main {
    public static void main(String[] args) {

        double mass = 70;
        double k = 10000;
        double gamma = 100;

        Force force = new Force() {
            @Override
            public Vector apply(double t) {
                throw new RuntimeException("Not implemented");
            }

            @Override
            public Vector apply(Vector position, Vector speed) {
                return position.multiply(-1*k).sub(speed.multiply(gamma));
            }
        };

        double dt = 0.01;
        Vector initialPos = new Vector(1,0);
        Vector initialSpeed = new Vector(-100.0/140,0);
        Vector previous = initialPos.sum(initialSpeed.multiply(-1*dt));

        VerletParameters parameters = new VerletParameters(initialPos,previous,initialSpeed,force,mass,dt,100);
        VerletAlgorithm algorithm = new VerletAlgorithm();

        EventsQueue eventsQueue = new EventsQueue();
        algorithm.calculate(parameters,eventsQueue::add);
        for (Event<?> event: eventsQueue) {
            VerletState state = (VerletState) event.getPayload();
            System.out.println("Pos: "+ state.getPosition() + " Speed: " + state.getSpeed());
        }
    }
}
