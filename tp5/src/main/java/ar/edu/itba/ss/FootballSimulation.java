package ar.edu.itba.ss;

import ar.edu.itba.ss.input.CSVIterator;
import ar.edu.itba.ss.input.FootballSimulationConfig;
import ar.edu.itba.ss.input.MatchStateMapper;
import ar.edu.itba.ss.models.GearPredictor;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SocialForce;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.output.CSVBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FootballSimulation {

    public static final String HOME_FILEPATH = "input/home.csv";
    public static final String AWAY_FILEPATH = "input/away.csv";

    public static final int[] HOME_POSITION_INDEXES = {4, 6, 8, 10, 14, 16, 18, 20, 22, 24, 26};
    public static final int HOME_BALL_INDEX = 32;

    public static final int[] AWAY_POSITION_INDEXES = {4, 6, 8, 10, 12, 14, 16, 18, 22, 24, 26};
    public static final int AWAY_BALL_INDEX = 28;

    private static final double DT = 1.0 / 240.0;
    private static final double FIELD_X = 105.0;
    private static final double FIELD_Y = 68;
    private static final double INITIAL_DISTANCE = 10;

    public static void simulate(String fileName, FootballSimulationConfig config){
        CSVBuilder builder = new CSVBuilder();
        try {
            builder.appendLine(fileName, "frame", "particle", "team", "x", "y");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MatchStateMapper homeMapper = new MatchStateMapper(HOME_POSITION_INDEXES,
                HOME_BALL_INDEX
        );

        MatchStateMapper awayMapper = new MatchStateMapper(AWAY_POSITION_INDEXES,
                AWAY_BALL_INDEX
        );
        GearPredictor gearPredictor = new GearPredictor(DT);

        try {
            CSVIterator homeIterator = new CSVIterator(HOME_FILEPATH);
            CSVIterator awayIterator = new CSVIterator(AWAY_FILEPATH);

            String[] homeRow = homeIterator.next();
            String[] awayRow = awayIterator.next();

            List<Particle> homeRowPlayers = homeMapper.getPlayers(homeRow);
            List<Particle> awayRowPlayers = awayMapper.getPlayers(awayRow);
            List<Particle> players = Stream.concat(homeRowPlayers.stream(), awayRowPlayers.stream()).collect(Collectors.toList());
            Vector ballPosition = awayMapper.getBall(awayRow);

            Particle lunatic = new Particle(
                    "lunatic",
                    config.getLunaticMass(),
                    config.getLunaticRadius(),
                    generateLunaticPosition(ballPosition),
                    new Vector(0, 0),
                    config.getDesiredSpeed(),
                    ballPosition
            );

            lunatic = GearPredictor.calculateFutureParticle(lunatic, SocialForce.calculateForce(lunatic, players, config.getTau()));

            int i = 0;
            while (homeIterator.hasNext() && awayIterator.hasNext() || (i % 10 != 0)) {

                if (i % 10 == 0 && i > 0) {
                    homeRow = homeIterator.next();
                    awayRow = awayIterator.next();
                    homeRowPlayers = homeMapper.getPlayers(homeRow);
                    awayRowPlayers = awayMapper.getPlayers(awayRow);
                    players = Stream.concat(homeRowPlayers.stream(), awayRowPlayers.stream()).collect(Collectors.toList());
                    ballPosition = awayMapper.getBall(awayRow);
                    lunatic = new Particle(lunatic.getName(), lunatic.getMass(), lunatic.getRadius(), lunatic.getR(), lunatic.getDesiredSpeed(), ballPosition);

                }

                Particle predicted = gearPredictor.predict(lunatic);
                Particle future = GearPredictor.calculateFutureParticle(predicted, SocialForce.calculateForce(lunatic, players, config.getTau()));
                Vector dR2 = gearPredictor.evaluate(predicted, future);
                Particle corrected = gearPredictor.correct(predicted, dR2);

                //Report state every dt2= 1/24 s
                if(i%10 == 0){
                    reportToCsv(builder,fileName,i,lunatic,ballPosition,homeRowPlayers,awayRowPlayers);
                }

                lunatic = corrected;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Vector generateLunaticPosition(Vector ballPosition) {
        Vector ballRealPosition = new Vector(ballPosition.getX() * FIELD_X, ballPosition.getY() * FIELD_Y);
        double y = Math.random() * INITIAL_DISTANCE;
        double x = Math.sqrt(INITIAL_DISTANCE * INITIAL_DISTANCE - y * y);
        double signX = Math.random() < 0.5 ? 1 : -1;
        double signY = Math.random() < 0.5 ? 1 : -1;
        x = signX * x;
        y = signY * y;

        Vector realLunaticPosition = new Vector(x, y).sum(ballRealPosition);
        return new Vector(realLunaticPosition.getX() / FIELD_X, realLunaticPosition.getY() / FIELD_Y);
    }

    private static void reportToCsv(CSVBuilder builder, String fileName, int i, Particle lunatic, Vector ballPosition, List<Particle> home, List<Particle> away) throws IOException {

        i = i/10;

        //Ball
        builder.appendLine(fileName,
                String.valueOf(i),
                "ball",
                "N/A",
                String.valueOf(ballPosition.getX()),
                String.valueOf(ballPosition.getY()));

        //Lunatic
        builder.appendLine(fileName,
                String.valueOf(i),
                lunatic.getName(),
                "N/A",
                String.valueOf(lunatic.getPosition().getX()),
                String.valueOf(lunatic.getPosition().getY()));

        //Home players
        for(Particle particle : home){
            builder.appendLine(fileName,
                    String.valueOf(i),
                    "N/A",
                    "home",
                    String.valueOf(particle.getPosition().getX()),
                    String.valueOf(particle.getPosition().getY()));
        }

        //Away players
        for(Particle particle : away){
            builder.appendLine(fileName,
                    String.valueOf(i),
                    "N/A",
                    "away",
                    String.valueOf(particle.getPosition().getX()),
                    String.valueOf(particle.getPosition().getY()));
        }
    }
}
