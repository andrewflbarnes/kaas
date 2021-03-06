package net.aflb.kaas.core.model.competing;

import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Round(
        long id,
        Date date,
        String name,
        Division division,
        League league,
        Set<Seeding> seeds,
        /**
         * When {@code true} then this contains no matches itself but other {@link #subRounds()},
         * when {@code false} then this contains {@link #matches()} to be run
         */
        boolean virtual,
        // One of
        List<Round> subRounds,
        List<Match> matches,
        List<Seeding> results
) {

    public static Round of(final boolean virtual, final String name, final Division division, final League league) {
        return new Round(
                // FIXME
                System.currentTimeMillis(),
                new Date(),
                name,
                division,
                league,
                new HashSet<>(),
                virtual,
                virtual ? Collections.emptyList() : null,
                virtual ? null : Collections.emptyList(),
                virtual ? null : Collections.emptyList());
    }

    public List<Match> matches() {
        if (virtual) {
            return subRounds.stream()
                    .map(Round::matches)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } else {
            return matches;
        }
    }

    public List<Match> matches(final Comparator<Match> comparator) {
        if (virtual) {
            return subRounds.stream()
                    .map(Round::matches)
                    .flatMap(List::stream)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } else {
            return matches;
        }
    }
}
