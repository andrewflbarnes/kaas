package net.aflb.kaas.core.model.competing;

import lombok.Data;
import net.aflb.kaas.core.model.Team;

@Data
public class Match implements Comparable<Match> {
    public enum Winner {
        ONE, TWO;
    }

    private final long id;
    private final Team teamOne;
    private final Team teamTwo;
    private Team winner;

    public static Match of(final Team teamOne, final Team teamTwo) {
        return new Match(
                // FIXME
                System.currentTimeMillis(),
                teamOne,
                teamTwo);
    }

    public void setWinner(final Winner winner) {
        this.winner = switch (winner) {
            case ONE -> teamOne;
            case TWO -> teamTwo;
        };
    }

    @Override
    public int compareTo(Match o) {
        return (int)(id - o.id);
    }
}
