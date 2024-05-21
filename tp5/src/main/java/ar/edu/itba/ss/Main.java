package ar.edu.itba.ss;

import ar.edu.itba.ss.input.CSVIterator;
import ar.edu.itba.ss.input.MatchStateMapper;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.io.IOException;
import java.util.List;

public class Main {

    public static final String HOME_FILEPATH = "input/home.csv";
    public static final String AWAY_FILEPATH = "input/away.csv";

    public static final int[] HOME_POSITION_INDEXES = {4, 6, 8, 10, 14, 16, 18, 20, 22, 24, 26};
    public static final int HOME_BALL_INDEX = 32;

    public static final int[] AWAY_POSITION_INDEXES = {4, 6, 8, 10, 12, 14, 16, 18, 22, 24, 26};
    public static final int AWAY_BALL_INDEX = 28;

    public static void main(String[] args) {

        MatchStateMapper homeMapper = new MatchStateMapper(HOME_POSITION_INDEXES,
                HOME_BALL_INDEX
        );

        MatchStateMapper awayMapper = new MatchStateMapper(AWAY_POSITION_INDEXES,
                AWAY_BALL_INDEX
        );

        try {
            CSVIterator homeIterator = new CSVIterator(HOME_FILEPATH);
            CSVIterator awayIterator = new CSVIterator(AWAY_FILEPATH);

            while (homeIterator.hasNext() && awayIterator.hasNext()) {
                String[] homeRow = homeIterator.next();
                String[] awayRow = awayIterator.next();

                List<Particle> homeRowPlayers = homeMapper.getPlayers(homeRow);
                List<Particle> awayRowPlayers = awayMapper.getPlayers(awayRow);
                Vector ballPosition = awayMapper.getBall(awayRow);

                // ACA HAY QUE HACER LAS COSITAS
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}