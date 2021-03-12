package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;

import java.util.List;

public interface MatchGenerator {
    List<Match<?>> generate(final Round round);
}
