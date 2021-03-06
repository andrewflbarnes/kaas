package net.aflb.kaas.core.legacy.export;

import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.Team;

import java.util.List;
import java.util.Optional;

/**
 * Well, let's hope this back of fuckery ends up working.
 *
 * @author Barnesly
 */
public interface MatchSerializer {

    record Result(
            boolean isError,
            Optional<Exception> error,
            // TODO bytestream/outputstream?
            Optional<byte[]> output
    ) {}

    /**
     * Generate some serialized form of a set of matches e.g. {@code byte[]} output representing a pdf.
     *
     * @param matches The ordered list of {@link Match}s which are to be added
     * @param teams The {@link List} of {@link Team}s which will be used to lookup team names
     * @returna {@link Result} containing either the raw {@code byte[]} output or an {@code Exception}
     */
    Result writeRaceList(List<Match> matches, List<Team> teams);
}
