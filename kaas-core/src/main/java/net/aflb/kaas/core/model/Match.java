package net.aflb.kaas.core.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Match implements Comparable<Match> {
    public enum Winner {
        ONE, TWO;
    }

    private final long id;
    private final Team teamOne;
    private final Team teamTwo;
    private Team winner;

    @Override
    public int compareTo(Match o) {
        return (int)(id - o.id);
    }

    public void setWinner(final Winner winner) {
        this.winner = switch (winner) {
            case ONE -> teamOne;
            case TWO -> teamTwo;
        };
    }
}
