package net.aflb.kaas.core.model.competing;

import lombok.Data;
import net.aflb.kaas.core.KaasID;
import net.aflb.kaas.core.model.Team;

import java.util.Optional;
import java.util.function.Supplier;

@Data
public class Match<T> {

    private static final Supplier<KaasID> GEN = KaasID.generator(Match.class);
    public enum Winner {
        ONE, TWO;
    }

    private final KaasID kassId;
    private final Team teamOne;
    private final Team teamTwo;
    private String teamOneDsq = null;
    private String teamTwoDsq = null;
    private Team winner = null;
    private T meta = null;

    public static Match of(final Team teamOne, final Team teamTwo) {
        return new Match(
                GEN.get(),
                teamOne,
                teamTwo);
    }

    public long getId() {
        return kassId.hashCode();
    }

    public void setWinner(final Winner winner) {
        this.winner = switch (winner) {
            case ONE -> teamOne;
            case TWO -> teamTwo;
        };
    }
}
