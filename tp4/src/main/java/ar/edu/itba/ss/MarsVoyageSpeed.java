package ar.edu.itba.ss;

import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.input.JsonConfigReader;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.TimeParameters;
import ar.edu.itba.ss.models.TimeUnits;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ar.edu.itba.ss.algorithms.gear.GearAlgorithmStep.factor;

public class MarsVoyageSpeed {

    private static final double GRAVITY_CONSTANT = 6.693 * Math.pow(10, -20);

    /**
     * Sun
     */
    private static final double SUN_MASS = 1.989 * Math.pow(10, 30);
    private static final double SUN_RADIUS = 696340;

    /**
     * Earth
     */
    private static final double EARTH_MASS = 5.972 * Math.pow(10, 24);
    private static final double EARTH_RADIUS = 6371;
    private static final double EARTH_X = -1.219024854566760E+08;
    private static final double EARTH_Y = -8.830999621339682E+07;
    private static final double EARTH_VX = 1.698154915953803E+01;
    private static final double EARTH_VY = -2.422995800936565E+01;

    /**
     * Mars
     */
    private static final double MARS_MASS = 6.39 * Math.pow(10, 23);
    private static final double MARS_RADIUS = 3389.5;
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

    public static void main(String[] args) throws IOException {

        JsonConfigReader configReader = new JsonConfigReader();
        TimeParameters timeParameters = configReader.readConfig("config.json", TimeParameters.class);

        ArgumentHandler argumentHandler = new ArgumentHandler();
        argumentHandler.addArgument("-O", (v) -> true, true, "output/");
        argumentHandler.parse(args);


        double stop = timeParameters.getStop();
        double dt = timeParameters.getDt();
        double printI = timeParameters.getPrintI();

        // Initialize CSV File
        String fileName = String.format(argumentHandler.getArgument("-O") + "min-distance-speed.csv", dt, timeParameters.getRawStart() + timeParameters.getStartUnits());
        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine(fileName, "speed", "distance", "iteration");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double maxSpeed = 9;
        double step = 0.001;

        ExecutorService executor = Executors.newFixedThreadPool(20);

        for (int s = 0; Double.compare( 7 + s * step, maxSpeed) <= 0; s++) {
            final int sFinal = s;
            executor.submit(() -> {

                double speed = 7 + step * sFinal;
                double start = TimeUnits.fromString("d").toSeconds(176) +
                        TimeUnits.fromString("h").toSeconds(8) +
                        TimeUnits.fromString("m").toSeconds(11);

                List<Particle> particles = new ArrayList<>();
                particles.add(new Particle("sun", SUN_MASS, SUN_RADIUS, new Vector(0, 0), new Vector(0, 0)));
                particles.add(new Particle("earth", EARTH_MASS, EARTH_RADIUS, new Vector(EARTH_X, EARTH_Y), new Vector(EARTH_VX, EARTH_VY)));
                particles.add(new Particle("mars", MARS_MASS, MARS_RADIUS, new Vector(MARS_X, MARS_Y), new Vector(MARS_VX, MARS_VY)));


                double time = 0;
                int i = 0;
                List<Particle> state = updateParticlesState(particles);

                double minDistance = Double.MAX_VALUE;
                int minTimeIteration = 0;

                while (Double.compare(time, start + stop) < 0) {

                    time = dt * i;
                    if (Double.compare(time, start) == 0) {
                        Particle particle = initializeSpaceship(state.get(1),speed);
                        particle = updateParticleState(particle, state);
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

                        Particle newParticle = new Particle(particle.getName(), particle.getMass(), particle.getRadius(), newR);
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

                        Particle correctedParticle = new Particle(predictedParticle.getName(), predictedParticle.getMass(), predictedParticle.getRadius(), newR);
                        correctedState.add(correctedParticle);
                    }

                    if (Double.compare(time, start) >= 0 && (i % printI == 0)) {
                        Particle marsState = state.get(2);
                        Particle spaceshipState = state.get(3);
                        double maybeMin = marsState.getPosition().sub(spaceshipState.getPosition()).norm();
                        minDistance = Math.min(minDistance, maybeMin);
                        if(Double.compare(minDistance, maybeMin) == 0)
                            minTimeIteration = i;
                    }

                    state = correctedState;
                    i++;

                }

                try {
                    builder.appendLine(
                            fileName,
                            String.valueOf(speed),
                            String.valueOf(minDistance),
                            String.valueOf(minTimeIteration)
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

            Particle updatedParticle = new Particle(current.getName(), current.getMass(), current.getRadius(), new ArrayList<>(current.getR()));

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
        Particle updatedParticle = new Particle(particle.getName(), particle.getMass(), particle.getRadius(), new ArrayList<>(particle.getR()));

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

    private static Particle initializeSpaceship(Particle earth, double speed) {
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
        double initialSpeed = tangentialSpeed + STATION_SPEED + speed;

        // Vector de velocidad inicial en la dirección tangencial
        Vector initialSpeedVector = new Vector(
                initialSpeed * perpendicular.getX(),
                initialSpeed * perpendicular.getY()
        );

        // Calcular la posición inicial de la nave espacial
        Vector initialPositionVector = new Vector(
                position.getX() + (STATION_DISTANCE + EARTH_RADIUS) * (position.getX()/position.norm()),
                position.getY() + (STATION_DISTANCE + EARTH_RADIUS) * (position.getY()/position.norm())
        );

        // Crear y devolver el objeto nave espacial
        return new Particle("spaceship", SPACESHIP_MASS, 0 ,initialPositionVector, initialSpeedVector);
    }


    public static int calculateIterationsForTwoYears(double deltaTimeInSeconds) {
        double secondsInTwoYears = 2 * 365 * 24 * 60 * 60;
        return (int) Math.ceil(secondsInTwoYears / deltaTimeInSeconds);
    }
}
