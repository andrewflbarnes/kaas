package net.aflb.kaas.kings.engine;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.competing.MetaMatch;
import net.aflb.kaas.kings.TestEntities;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class StandardMatchListGeneratorTest {

    @Test
    void separateDivisionList() {
        final var target = StandardMatchListGenerator.newBuilder()
                .withGroup(TestEntities.MIXED)
                .withGroup(TestEntities.LADIES)
                .withGroup(TestEntities.BOARD)
                .build();

        final var result = target.generate(TestEntities.ROUND1.subRounds().get(0));
//        log(result);

        assertEquals(36, result.size());
        assertTrue(result.subList(0, 12).stream().allMatch(m -> TestEntities.MIXED.equals(m.getDivision())));
        assertTrue(result.subList(12, 24).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(24, 36).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
    }

    @Test
    void interspersedDivisionList() {
        final var target = StandardMatchListGenerator.newBuilder()
                .withGroup(TestEntities.MIXED, TestEntities.LADIES, TestEntities.BOARD)
                .build();

        final var result = target.generate(TestEntities.ROUND1.subRounds().get(0));
//        log(result);

        assertEquals(36, result.size());
        assertTrue(result.subList(0, 4).stream().allMatch(m -> TestEntities.MIXED.equals(m.getDivision())));
        assertTrue(result.subList(4, 8).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(8, 12).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
        assertTrue(result.subList(12, 16).stream().allMatch(m -> TestEntities.MIXED.equals(m.getDivision())));
        assertTrue(result.subList(16, 20).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(20, 24).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
        assertTrue(result.subList(24, 28).stream().allMatch(m -> TestEntities.MIXED.equals(m.getDivision())));
        assertTrue(result.subList(28, 32).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(32, 36).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
    }

    @Test
    void partialInterspersedDivisionList() {
        final var target = StandardMatchListGenerator.newBuilder()
                .withGroup(TestEntities.MIXED)
                .withGroup(TestEntities.LADIES, TestEntities.BOARD)
                .build();

        final var result = target.generate(TestEntities.ROUND1.subRounds().get(0));
        log(result);

        assertEquals(36, result.size());
        assertTrue(result.subList(0, 12).stream().allMatch(m -> TestEntities.MIXED.equals(m.getDivision())));
        assertTrue(result.subList(12, 16).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(16, 20).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
        assertTrue(result.subList(20, 24).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(24, 28).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
        assertTrue(result.subList(28, 32).stream().allMatch(m -> TestEntities.LADIES.equals(m.getDivision())));
        assertTrue(result.subList(32, 36).stream().allMatch(m -> TestEntities.BOARD.equals(m.getDivision())));
    }

    private void log(List<MetaMatch> result) {
        result.forEach(mm -> {
            log.info("MATCH: {} {} {} - {} v {}",
                    spaced(mm.getLeague().name(), -15),
                    spaced(mm.getDivision().name(), -15),
                    spaced(mm.getMinileague(), -5),
                    spaced(mm.getMatch().getTeamOne().name(), -20),
                    spaced(mm.getMatch().getTeamTwo().name(), 20));
        });
    }

    private String spaced(String toPad, int padding) {
        return String.format("%" + padding + "s", toPad);
    }
}