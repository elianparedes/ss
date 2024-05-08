package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;
import ar.edu.itba.ss.simulation.events.Event;
import ar.edu.itba.ss.simulation.events.EventsQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.ss.algorithms.gear.GearAlgorithmStep.factor;

public class MarsVoyage {

    private static final int MAX_ITERATIONS = 365*2*24*2;
    private static final double DT_STEP = 86400/24*2;
    private static final double GRAVITY_CONSTANT = 6.693 * Math.pow(10, -20);

    /**
     * Sun
     */
    private static final double SUN_MASS = 1.989 * Math.pow(10, 30);

    /**
     * Earth
     */
    private static final double EARTH_MASS = 5.972 * Math.pow(10, 24);
    private static final double EARTH_X = -1.219024854566760E+08;
    private static final double EARTH_Y = -8.830999621339682E+07;
    private static final double EARTH_VX = 1.698154915953803E+01;
    private static final double EARTH_VY = -2.422995800936565E+01;

    private static final double EARTH_RADIUS = 6378;

    /**
     * Mars
     */
    private static final double MARS_MASS = 6.39 * Math.pow(10, 23);
    private static final double MARS_X = 1.758500774292310E+08;
    private static final double MARS_Y = -1.086968363813986E+08;
    private static final double MARS_VX = 1.365943796448699E+01;
    private static final double MARS_VY = 2.268050972064907E+01;

    /**
     * SpaceShip
     */

    private static final double SPACESHIP_MASS = 200000;
    private static final double SPACESHIP_SPEED=8;
    private static final double STATION_DISTANCE=1500;
    private static final double STATION_SPEED=7.12;




    private static final double[] ALPHAS = new double[]{(3.0 / 20.0), (251.0 / 360), 1.0, (11.0 / 18.0), (1.0 / 6.0), (1.0 / 60.0)};

    public static void main(String[] args) {
        EventsQueue events = new EventsQueue();
        List<Particle> particles = new ArrayList<>();
        particles.add(new Particle("sun", SUN_MASS, new Vector(0,0), new Vector(0,0)));
        particles.add(new Particle("earth", EARTH_MASS, new Vector(EARTH_X, EARTH_Y), new Vector(EARTH_VX, EARTH_VY)));
        particles.add(new Particle("mars", MARS_MASS, new Vector(MARS_X, MARS_Y), new Vector(MARS_VX, MARS_VY)));

        int iteration = 172*24*2;

        double dt = DT_STEP;
        double time = 0;
        List<Particle> state = updateParticlesState(particles);

        for (int i = 0; i < MAX_ITERATIONS + iteration; i++) {
            time = dt * i;

            if(i == iteration){
                Particle particle = initializeSpaceship(state.get(1));
                particle = updateParticleState(particle,state);
                state.add(particle);
            }

            // Predict
            List<Particle> predictedState = new ArrayList<>();
            for (Particle particle : state) {
                List<Vector> r = particle.getR();
                List<Vector> newR = new ArrayList<>(r);

                for (int j = 0; j < r.size(); j++) {
                    for (int k = j + 1; k < r.size(); k++) {
                        newR.set(j, newR.get(j).sum(r.get(k).multiply(factor(dt, k - j))));
                    }
                }

                Particle newParticle = new Particle(particle.getName(), particle.getMass(), newR);
                predictedState.add(newParticle);
            }

            // Evaluate
            List<Particle> futureState = updateParticlesState(predictedState);
            List<Vector> dR2s = new ArrayList<>();
            for (int j = 0; j < futureState.size(); j++) {
                Particle futureParticle = futureState.get(j);
                Particle predictedParticle = predictedState.get(j);

                Vector futureA = futureParticle.getAcceleration();
                Vector dA = futureA.sub(predictedParticle.getAcceleration());

                Vector dR2 = dA.multiply(factor(dt, 2));
                dR2s.add(dR2);
            }

            // Correct
            List<Particle> correctedState = new ArrayList<>();
            for (int j = 0; j < predictedState.size(); j++) {
                Particle predictedParticle = predictedState.get(j);

                List<Vector> r = predictedParticle.getR();
                List<Vector> newR = new ArrayList<>(r);

                for (int k = 0; k < predictedParticle.getR().size(); k++) {
                    newR.set(k, newR.get(k).sum(dR2s.get(j).multiply(ALPHAS[k]).divide(factor(dt, k))));
                }

                Particle correctedParticle = new Particle(predictedParticle.getName(), predictedParticle.getMass(), newR);
                correctedState.add(correctedParticle);
            }

            if(i >= iteration)
                events.add(new Event<>(new MarsVoyageState(state, dt, DT_STEP * i)));

            state = correctedState;
        }

        events.add(new Event<>(new MarsVoyageState(state, dt, time)));


        // CSV
        String fileName = "output/solarium-pericardium.csv";
        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine(fileName, "dt", "time", "name", "x", "y");

            for (Event<?> event:
                 events) {
                MarsVoyageState marsVoyageState = (MarsVoyageState) event.getPayload();
                List<Particle> particlesStates = marsVoyageState.getParticlesState();
                for (Particle particleState:
                     particlesStates) {
                    builder.appendLine(
                            fileName,
                            String.valueOf(marsVoyageState.getDt()),
                            String.valueOf(marsVoyageState.getTime()),
                            String.valueOf(particleState.getName()),
                            String.valueOf(particleState.getPosition().getX()),
                            String.valueOf(particleState.getPosition().getY())
                    );
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Particle> updateParticlesState(final List<Particle> particles) {
        List<Particle> updatedParticles = new ArrayList<>();

        for (Particle current : particles) {
            List<Vector> forces = new ArrayList<>();
            for (Particle other : particles) {
                if (!current.equals(other) && !current.getName().equals("sun")) {
                    forces.add(calculateForce(other.getPosition().sub(current.getPosition()),
                            current.getMass(),
                            other.getMass()));
                }
            }

            Particle updatedParticle = new Particle(current.getName(), current.getMass(), new ArrayList<>(current.getR()));

            updatedParticle.setVelocity(current.getVelocity());
            updatedParticle.setPosition(current.getPosition());
            updatedParticle.setAcceleration(sumForces(forces).divide(current.getMass()));

            updatedParticles.add(updatedParticle);
        }

        return updatedParticles;
    }

    private static Particle updateParticleState(final Particle particle, final List<Particle> particles){
        List<Vector> forces = new ArrayList<>();
        for (Particle other : particles) {
            if (!particle.equals(other) && !particle.getName().equals("sun")) {
                forces.add(calculateForce(other.getPosition().sub(particle.getPosition()),
                        particle.getMass(),
                        other.getMass()));
            }
        }
        Particle updatedParticle = new Particle(particle.getName(), particle.getMass(), new ArrayList<>(particle.getR()));

        updatedParticle.setVelocity(particle.getVelocity());
        updatedParticle.setPosition(particle.getPosition());
        updatedParticle.setAcceleration(sumForces(forces).divide(particle.getMass()));

        return updatedParticle;
    }

    private static Vector calculateForce(Vector position, double m1, double m2) {
        double angle = position.angle();
        double forceModule = GRAVITY_CONSTANT * m1 * m2 / position.norm2();
        return new Vector(forceModule * Math.cos(angle), forceModule * Math.sin(angle));
    }

    private static Vector sumForces(List<Vector> forces) {
        Vector totalForce = new Vector(0, 0);
        for (Vector force : forces) {
            totalForce = totalForce.sum(force);
        }
        return totalForce;
    }

    private static Particle initializeSpaceship(Particle earth) {
        // Obtener el vector posición de la Tierra
        Vector position = earth.getPosition();
        double distance = position.norm();

        // Calcular el vector perpendicular (tangencial) a la posición de la Tierra, en sentido opuesto
        Vector perpendicular = new Vector(-1*position.getY(), position.getX());

        // Normalizar el vector tangencial
        perpendicular = perpendicular.divide(perpendicular.norm());

        // Proyectar la velocidad de la Tierra en la dirección tangencial
        double tangentialSpeed = earth.getVelocity().getX() * perpendicular.getX() +
                earth.getVelocity().getY() * perpendicular.getY();

        // Sumar las velocidades de la estación y la nave espacial
        double initialSpeed = tangentialSpeed + STATION_SPEED + SPACESHIP_SPEED;

        // Vector de velocidad inicial en la dirección tangencial
        Vector initialSpeedVector = new Vector(
                initialSpeed * perpendicular.getX(),
                initialSpeed * perpendicular.getY()
        );

        // Calcular la posición inicial de la nave espacial
        Vector initialPositionVector = new Vector(
                distance * position.getX() / position.norm() + (STATION_DISTANCE + EARTH_RADIUS) * perpendicular.getX(),
                distance * position.getY() / position.norm() + (STATION_DISTANCE + EARTH_RADIUS) * perpendicular.getY()
        );

        // Crear y devolver el objeto nave espacial
        return new Particle("spaceship", SPACESHIP_MASS, initialPositionVector, initialSpeedVector);
    }


}
