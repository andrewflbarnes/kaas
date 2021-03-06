package net.aflb.kaas.core.model.competing;

import lombok.Data;
import net.aflb.kaas.core.KaasID;
import net.aflb.kaas.core.model.Team;

import java.util.function.Supplier;

@Data
public class Match<T> {

    private static final Supplier<KaasID> GEN = KaasID.generator(Match.class);
    public enum Winner {
        ONE, TWO;
    }

    private final KaasID id;
    private final Team teamOne;
    private final Team teamTwo;
    private String teamOneDsq = null;
    private String teamTwoDsq = null;
    private Team winner = null;
    private T meta = null;

    public static <T> Match<T> of(final Team teamOne, final Team teamTwo) {
        return new Match<>(
                GEN.get(),
                teamOne,
                teamTwo);
    }

    public void setWinner(final Winner winner) {
        this.winner = switch (winner) {
            case ONE -> teamOne;
            case TWO -> teamTwo;
        };
    }

    public boolean isComplete() {
        return this.winner != null;
    }
}
