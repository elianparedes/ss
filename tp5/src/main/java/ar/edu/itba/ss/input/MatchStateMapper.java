package ar.edu.itba.ss.input;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;

public class MatchStateMapper {

    public static final double PLAYER_MASS = 80;
    public static final double PLAYER_RADIUS = 0.25;
    public static final int FRAME_INDEX = 2;
    public static final int TIME_INDEX = 3;

    private final int[] playerPositionIndexes;
    private final int[] playersNumbers;
    private final int ballIndex;


    public MatchStateMapper(int[] playerPositionIndexes, int[] playersNumbers, int ballIndex) {
        this.playerPositionIndexes = playerPositionIndexes;
        this.playersNumbers = playersNumbers;
        this.ballIndex = ballIndex;
    }

    public List<Particle> getPlayers(String[] row) {
        List<Particle> players = new ArrayList<>();

        for (int i = 0; i < playerPositionIndexes.length; i++) {
            int playerPositionIndex = playerPositionIndexes[i];
            double x = Double.parseDouble(row[playerPositionIndex]);
            double y = Double.parseDouble(row[playerPositionIndex + 1]);

            Particle player = new Particle(
                    String.format("%s", playersNumbers[i]),
            PLAYER_MASS,
                    PLAYER_RADIUS,
                    new Vector(x, y),
                    new Vector(0f, 0f),
                    0,
                    null
            );

            players.add(player);
        }

        return players;
    }

    public Vector getBall(String[] row) {
        return new Vector(Double.parseDouble(row[ballIndex]), Double.parseDouble(row[ballIndex + 1]));
    }

    public double getFrame(String[] row) {
        return Integer.parseInt(row[FRAME_INDEX]);
    }

    public double getTime(String[] row) {
        return Double.parseDouble(row[TIME_INDEX]);
    }
}
