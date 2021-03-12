package net.aflb.kaas.core.model.competing;

import net.aflb.kaas.core.KaasID;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record Round(
        KaasID id,
        Date date,
        String name,
        League league,
        Map<Division, List<Team>> seeds,
        /**
         * When {@code true} then this contains no matches itself but other {@link #subRounds()},
         * when {@code false} then this contains {@link #matches()} to be run
         */
        boolean virtual,
        // One of
        List<Round> subRounds,
        List<Match<?>> matches
) {
    private static final Supplier<KaasID> GEN = KaasID.generator(Round.class);
    private static final String DEBUG_PREFIX = ">";

    public static Round of(final boolean virtual, final String name, final Map<Division, List<Team>> seeds, final League league) {
        return new Round(
                // FIXME
                GEN.get(),
                new Date(),
                name,
                league,
                seeds,
                virtual,
                virtual ? new ArrayList<>() : null,
                virtual ? null : new ArrayList<>());
    }

    public List<Division> divisions() {
        // We may want to cache this if we start using it a lot...
        return new ArrayList<>(seeds.keySet());
    }

    public Optional<Division> division() {
        final var divisions = divisions();
        return divisions().size() == 1
                ? Optional.of(divisions.get(0))
                : Optional.empty();
    }

    public List<Match<?>> matches() {
        if (virtual) {
            return subRounds.stream()
                    .map(Round::matches)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } else {
            return matches;
        }
    }

    public List<Match<?>> matches(final Comparator<Match<?>> comparator) {
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

    public Map<Division, List<Team>> teamRankings() {
        return seeds.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream()
                        .map(t -> new Ranking<>(t, getTeamWins(t)))
                        .sorted(Ranking.BY_WEIGHT)
                        .map(Ranking::entity)
                        .collect(Collectors.toList())));
    }

    public long getTeamWins(final Team team) {
        return matches().stream()
                .filter(m -> team.equals(m.getWinner()))
                .count();
    }

    public boolean isComplete() {
        if (virtual) {
            return subRounds.stream().allMatch(Round::isComplete);
        } else {
            return matches.stream().allMatch(Match::isComplete);
        }
    }

    public String debug() {
        return debug(DEBUG_PREFIX, "");
    }

    protected String debug(final String prefix, final String label) {
        final var labeledName = label.isEmpty() ? name : "%s | %s".formatted(label, name);
        var debugOutput = "\n%s %s".formatted(prefix, labeledName);
        if (this.virtual()) {
            debugOutput += subRounds.stream().map(sr -> sr.debug(prefix + prefix.charAt(0), labeledName))
                    .collect(Collectors.joining());
        } else {
            debugOutput += matches.stream().map(m -> "\n%s %s v %s".formatted(prefix, m.getTeamOne().name(), m.getTeamTwo().name()))
                    .collect(Collectors.joining());
        }

        return debugOutput;
    }
}
