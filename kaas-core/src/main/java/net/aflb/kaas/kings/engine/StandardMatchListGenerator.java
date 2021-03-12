package net.aflb.kaas.kings.engine;

import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.competing.MetaMatch;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.spi.MatchListGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO rename to PartitionedMatchListGenerator?
// TODO define a nesting level so this i more generic?
public class StandardMatchListGenerator implements MatchListGenerator {
    @Override
    public List<MetaMatch> generate(Round set) {
        final var league = set.league();
        final List<List<MetaMatch>> partitions = new ArrayList<>();
        set.subRounds().forEach(setDivision -> {
            final var division = setDivision.division().orElse(Division.NONE);
            setDivision.subRounds().forEach(group -> {
                for (int i = 0; i < group.subRounds().size(); i++) {
                    final var partition = group.subRounds().get(i);
                    if (partitions.size() < i + 1) {
                        partitions.add(new ArrayList<>());
                    }
                    partitions.get(i).addAll(partition.matches().stream()
                            .map(m -> new MetaMatch(league, division, m))
                            .collect(Collectors.toList()));
                }
            });
        });

        return partitions.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
