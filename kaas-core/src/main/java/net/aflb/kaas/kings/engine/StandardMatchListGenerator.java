package net.aflb.kaas.kings.engine;

import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.competing.MetaMatch;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.spi.MatchListGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO rename to PartitionedMatchListGenerator?
// TODO define a nesting level so this i more generic?
public class StandardMatchListGenerator implements MatchListGenerator {
    private static final Comparator<Map.Entry<Division, ?>> DIVISION_ORDER = (e1, e2) -> {
        final var e1Name = e1.getKey().name().toLowerCase();
        final var e2Name = e2.getKey().name().toLowerCase();
        if (e1Name.equalsIgnoreCase(e2Name)) {
            return 0;
        }
        return -e1Name.compareTo(e2Name);
    };

    @Override
    public List<MetaMatch> generate(Round set) {
        final var league = set.league();
        final List<Map<Division, List<MetaMatch>>> partitions = new ArrayList<>();
        set.subRounds().forEach(setDivision -> {
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
                                .collect(Collectors.toList()));
                        return mms;
                    });
                }
            });
        });

        return partitions.stream()
                .flatMap(p -> p.entrySet().stream().sorted(DIVISION_ORDER))
                .flatMap(p -> p.getValue().stream())
                .collect(Collectors.toList());
    }
}
