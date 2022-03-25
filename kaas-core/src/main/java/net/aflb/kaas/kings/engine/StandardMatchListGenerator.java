package net.aflb.kaas.kings.engine;

import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.competing.MetaMatch;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.spi.MatchListGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Returns a race list ordered by partition then division (Mixed, Ladies, Board). e.g.
 * for the races.
 * <ul>
 * <li>Ladies
 *   <ul>
 *     <li>p1: r1 r2</li>
 *     <li>p2: r1 r2</li>
 *   </ul>
 * </li>
 * <li>Board
 *   <ul>
 *     <li>p1: r1 r2</li>
 *     <li>p2: r1 r2</li>
 *   </ul>
 * </li>
 * <li>Mixed
 *   <ul>
 *     <li>p1: r1 r2</li>
 *     <li>p2: r1 r2</li>
 *   </ul>
 * </li>
 * </ul>
 * We will receive
 * <ul>
 * <li>p1 Mixed r1</li>
 * <li>p1 Mixed r2</li>
 * <li>p1 Ladies r1</li>
 * <li>p1 Ladies r2</li>
 * <li>p1 Board r1</li>
 * <li>p1 Board r2</li>
 * <li>p2 Mixed r1</li>
 * <li>p2 Mixed r2</li>
 * <li>p2 Ladies r1</li>
 * <li>p2 Ladies r2</li>
 * <li>p2 Board r1</li>
 * <li>p2 Board r2</li>
 * </ul>
 */
// TODO rename to PartitionedMatchListGenerator?
// TODO define a nesting level so this i more generic?
public class StandardMatchListGenerator implements MatchListGenerator {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        boolean knockout = false;
        final List<List<Division>> groups = new ArrayList<>();

        protected Builder() {
        }

        public Builder asKnockout() {
            return asKnockout(true);
        }

        public Builder asKnockout(boolean knockout) {
            this.knockout = knockout;
            return this;
        }

        public Builder withGroup(Division... divisions) {
            final var divisionList = List.of(divisions);
            final var existingDivisions = groups.stream()
                    .flatMap(List::stream)
                    .toList();
            divisionList.forEach(d -> {
                if (existingDivisions.contains(d)) {
                    throw new IllegalStateException("Division already present: " + d.name());
                }
            });
            groups.add(divisionList);
            return this;
        }

        public StandardMatchListGenerator build() {
            return new StandardMatchListGenerator(knockout, Collections.unmodifiableList(groups));
        }
    }

    final List<List<Division>> divisionOrdering;
    /**
     * A comparator which sorts entries by the Kings division order. For races this is
     * Mixed -> Ladies -> Board which is fortunately just reverse alphabetical!
     */
    private static final Comparator<Map.Entry<Division, ?>> DIVISION_COMPARATOR =
            Comparator.comparing((Map.Entry<Division, ?> e) -> e.getKey().name().toLowerCase()).reversed();
    private static final List<Character> KNOCKOUT_DIVISION_ORDER = List.of('l', 'b', 'm');
    private static final Comparator<Map.Entry<Division, ?>> KNOCKOUT_DIVISION_COMPARATOR =
            Comparator.comparing((Map.Entry<Division, ?> e) -> KNOCKOUT_DIVISION_ORDER.indexOf(e.getKey().name().toLowerCase().charAt(0)));
    private static final List<String> GROUP_ORDER = List.of("I", "II", "III", "IV", "V", "VI", "VII", "VIII");
    // TODO (we currently rely on a LinkedHashMap in SetTwoMatchGenerator - is that enough?)
    private static final Comparator<Round> GROUP_COMPARATOR = (r1, r2) -> {
        final var g1 = r1.name().toUpperCase();
        final var g2 = r2.name().toUpperCase();
        if (GROUP_ORDER.contains(g1) && GROUP_ORDER.contains(g2)) {
            return GROUP_ORDER.indexOf(g1) - GROUP_ORDER.indexOf(g2);
        }
        return g1.compareTo(g2);
    };

    private final Comparator<Map.Entry<Division, ?>> comparator;

    public StandardMatchListGenerator(final boolean knockout, List<List<Division>> divisionOrdering) {
        this.comparator = knockout ? KNOCKOUT_DIVISION_COMPARATOR : DIVISION_COMPARATOR;
        this.divisionOrdering = divisionOrdering;
    }

    @Override
    public List<MetaMatch> generate(Round set) {
        final var league = set.league();

        final var divisions = set.subRounds().stream()
                .collect(Collectors.toMap(
                        d -> d.division().orElse(Division.NONE),
                        Function.identity()));

        return divisionOrdering.stream()
                .map(g -> partitionedMatchList(g.stream().map(divisions::get).toList(), league))
                .flatMap(List::stream)
                .toList();
    }

    public List<MetaMatch> partitionedMatchList(List<Round> divisions, League league) {
        final List<Map<Division, List<MetaMatch>>> partitions = new ArrayList<>();
        divisions.forEach(setDivision -> {
            final var division = setDivision.division().orElse(Division.NONE);
            setDivision.subRounds().forEach(group -> {
                final var groupName = group.name();
                for (int i = 0; i < group.subRounds().size(); i++) {
                    final var partition = group.subRounds().get(i);
                    if (partitions.size() < i + 1) {
                        partitions.add(new HashMap<>());
                    }
                    partitions.get(i).compute(division, (k, mms) -> {
                        if (mms == null) {
                            mms = new ArrayList<>();
                        }
                        mms.addAll(partition.matches().stream()
                                .map(m -> new MetaMatch(league, division, groupName, m))
                                .toList());
                        return mms;
                    });
                }
            });
        });

        return partitions.stream()
                .flatMap(p -> p.entrySet().stream().sorted(comparator))
                .flatMap(p -> p.getValue().stream())
                .collect(Collectors.toList());
    }
}
