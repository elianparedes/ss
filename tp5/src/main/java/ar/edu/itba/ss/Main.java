package ar.edu.itba.ss;

import ar.edu.itba.ss.input.CSVIterator;
import ar.edu.itba.ss.input.MatchStateMapper;
import ar.edu.itba.ss.models.GearPredictor;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SocialForce;
import ar.edu.itba.ss.models.Vector;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static final String HOME_FILEPATH = "input/home.csv";
    public static final String AWAY_FILEPATH = "input/away.csv";

    public static final int[] HOME_POSITION_INDEXES = {4, 6, 8, 10, 14, 16, 18, 20, 22, 24, 26};
    public static final int HOME_BALL_INDEX = 32;

    public static final int[] AWAY_POSITION_INDEXES = {4, 6, 8, 10, 12, 14, 16, 18, 22, 24, 26};
    public static final int AWAY_BALL_INDEX = 28;

    private static final double DT = 1.0 / 240.0;

    public static void main(String[] args) {
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
                    80.0,
                    0.25,
                    generateLunaticPosition(ballPosition),
                    new Vector(0, 0),
                    3.0,
                    ballPosition
            );

            lunatic = GearPredictor.calculateFutureParticle(lunatic,SocialForce.calculateForce(lunatic,players,0.5));

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
                Particle future = GearPredictor.calculateFutureParticle(predicted,SocialForce.calculateForce(lunatic,players,0.5));
                Vector dR2 = gearPredictor.evaluate(predicted,future);
                Particle corrected = gearPredictor.correct(predicted,dR2);

                System.out.println(lunatic.getPosition());
                //Maybe export some state before
                lunatic = corrected;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Vector generateLunaticPosition(Vector ballPosition) {
        Vector ballRealPosition = new Vector(ballPosition.getX() * 105, ballPosition.getY() * 68);
        double y = Math.random() * 10;
        double x = Math.sqrt(10 * 10 - y * y);
        double signX = Math.random() < 0.5 ? 1 : -1;
        double signY = Math.random() < 0.5 ? 1 : -1;
        x = signX * x;
        y = signY * y;

        Vector realLunaticPosition = new Vector(x, y).sum(ballRealPosition);
        return new Vector(realLunaticPosition.getX() /105.0, realLunaticPosition.getY() / 68.0);
    }
}