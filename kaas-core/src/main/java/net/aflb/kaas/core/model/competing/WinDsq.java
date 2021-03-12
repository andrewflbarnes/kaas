package net.aflb.kaas.core.model.competing;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.aflb.kaas.core.model.Team;

import java.util.Comparator;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Data
// TODO better name
public class WinDsq {
    private static final int DEFAULT_WEIGHT_WIN = 100;
    private static final int DEFAULT_WEIGHT_DSQ = 10;
    private static final int DEFAULT_WEIGHT_ADJUSTMENT = 1;

    public static final Comparator<WinDsq> WEIGHTED = Comparator.comparingInt(WinDsq::weighting).reversed();

    public static Function<Team, WinDsq> factory() {
        return customWeightedFactory(DEFAULT_WEIGHT_WIN, DEFAULT_WEIGHT_DSQ, DEFAULT_WEIGHT_ADJUSTMENT);
    }

    public static Function<Team, WinDsq> customWeightedFactory(final int weightWin, final int weightDsq, final int weightAdjustment) {
        return t -> new WinDsq(t, weightWin, weightDsq, weightAdjustment);
    }

    private final Team team;
    private final int weightWin;
    private final int weightDsq;
    private final int weightAdjustment;
    private int wins = 0;
    private int dsqs = 0;
    private int adjustments = 0;

    public int weighting() {
        return weightWin * wins - weightDsq * dsqs + weightAdjustment * adjustments;
    }

    public int addWin() {
        this.wins++;
        return wins;
    }

    public int addDsq() {
        this.dsqs++;
        return dsqs;
    }

    public int addAdjustment() {
        this.adjustments++;
        return adjustments;
    }
}
