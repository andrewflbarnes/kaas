package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.competing.MetaMatch;
import net.aflb.kaas.core.model.competing.Round;

import java.util.List;

public interface MatchListGenerator {
    List<MetaMatch> generate(Round round);
}
