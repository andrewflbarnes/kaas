package net.aflb.kaas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.Registry;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.serialization.JacksonSerializationUtils;
import net.aflb.kaas.core.spi.MatchResultProcessor;
import net.aflb.kaas.engine.BasicMatchResultProcessor;
import net.aflb.kaas.kings.engine.SetOneMatchGenerator;
import net.aflb.kaas.kings.engine.SetTwoMatchGenerator;
import net.aflb.kaas.kings.engine.StandardMatchListGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SmokeTest {

    @Test
    void test() throws Exception {
        final Registry registry = new Registry();
        final League league = League.of("Northern");
        final Division division1 = Division.of("Mixed", 1);
        final Division division2 = Division.of("Ladies", 2);
        final Club club1 = Club.of("Kings");
        Team team11 = Team.of("Kings 1");
        Team team12 = Team.of("Kings 2");
        Team team13 = Team.of("Kings 3");
        Team team14 = Team.of("Kings 4");
        final Club club2 = Club.of("Southampton 2");
        Team team21 = Team.of("Southampton 1");
        Team team22 = Team.of("Southampton 2");
        Team team23 = Team.of("Southampton 3");
        Team team24 = Team.of("Southampton 4");
        final Club club3 = Club.of("UWE 3");
        Team team31 = Team.of("UWE 1");
        Team team32 = Team.of("UWE 2");
        Team team33 = Team.of("UWE 3");
        final Club club4 = Club.of("Bath 4");
        Team team41 = Team.of("Bath 1");
        final Club club5 = Club.of("NUSSC 5");
        Team team51 = Team.of("NUSSC 1");
        final Club club6 = Club.of("SKUM 6");
        Team team61 = Team.of("SKUM 1");
        Team team62 = Team.of("SKUM 2");
        Team team63 = Team.of("SKUM 3");
        registry.registerLeague(league)

                .registerDivision(division1)
                .registerDivision(division2)

                .registerClub(club1, league)
                .registerClub(club2, league)
                .registerClub(club3, league)
                .registerClub(club4, league)
                .registerClub(club5, league)
                .registerClub(club6, league)

                .registerTeam(team11, club1, division1)
                .registerTeam(team12, club1, division1)
                .registerTeam(team13, club1, division1)
                .registerTeam(team14, club1, division1)

                .registerTeam(team21, club2, division1)
                .registerTeam(team22, club2, division1)
                .registerTeam(team23, club2, division1)
                .registerTeam(team24, club2, division1)

                .registerTeam(team31, club3, division2)
                .registerTeam(team32, club3, division2)
                .registerTeam(team33, club3, division2)

                .registerTeam(team41, club4, division2)

                .registerTeam(team51, club5, division2)

                .registerTeam(team61, club6, division2)
                .registerTeam(team62, club6, division2)
                .registerTeam(team63, club6, division2);

        // we use this as the "strategy" in getSeeds. In the real world we'd use actual seed information and a more
        // complex comparator.
        final Map<Team, Integer> seeding = new HashMap<>();
        seeding.put(team11, 10);
        seeding.put(team21, 2);
        seeding.put(team12, 3);
        seeding.put(team13, 4);
        seeding.put(team14, 1);
        seeding.put(team22, 6);
        seeding.put(team23, 7);
        seeding.put(team24, 8);

        seeding.put(team31, 1);
        seeding.put(team32, 2);
        seeding.put(team33, 3);
        seeding.put(team41, 4);
        seeding.put(team51, 5);
        seeding.put(team61, 6);
        seeding.put(team62, 7);
        seeding.put(team63, 8);

        final var seeds = registry.getSeeds(Comparator.comparingInt(seeding::get));
        final var leagueSeeds = seeds.get(league);
        final var round = Round.of(true, "round", leagueSeeds, league);
        var result = new SetOneMatchGenerator().generate(round);

//        result.forEach(m -> log.info("{} v {}", m.getTeamOne().name(), m.getTeamTwo().name()));

        log.info("ROUND DEBUG\n{}", round.debug());
//        print(round, ">");

        // Assume the kings implementation for now
        // so 2 divisions with 8 teams on round 1
        // per division that's 2 mini leagues with 6 races - so 24 races total
        assertNotNull(round.matches());
        assertEquals(24, round.matches().size());
        // TODO verify grouping - don't bother yet as we are missing match metadata

        // slightly deeper checks
        final var sets = round.subRounds();
        assertNotNull(sets);
        assertEquals(1, sets.size());

        final var set1 = sets.get(0);

        final var set1divisions = set1.subRounds();
        assertNotNull(set1divisions);
        assertEquals(2, set1divisions.size());

        // check ranking
        final var seconds = List.of(team22, team62);
        round.matches().forEach(m -> {
            if (seconds.contains(m.getTeamOne())) {
                m.setWinner(Match.Winner.ONE);
            }
            if (seconds.contains(m.getTeamTwo())) {
                m.setWinner(Match.Winner.TWO);
            }
        });
        final var winners = List.of(team13, team51);
        round.matches().forEach(m -> {
            if (winners.contains(m.getTeamOne())) {
                m.setWinner(Match.Winner.ONE);
            }
            if (winners.contains(m.getTeamTwo())) {
                m.setWinner(Match.Winner.TWO);
            }
        });
        final var actualSeconds = new ArrayList<Team>();
        final var actualWinners = new ArrayList<Team>();
        set1divisions.forEach(d -> d.teamRankings().values().forEach(ts -> {
            actualWinners.add(ts.get(0));
            actualSeconds.add(ts.get(1));
        }));
        assertEquals(winners, actualWinners);
        assertEquals(seconds, actualSeconds);
//        divisions.forEach(d -> d.teamRankings().forEach((div, ts) -> {
//            log.info("{} {}", div.name(), ts.get(0));
//            log.info("{} {}", div.name(), ts.get(1));
//        }));

        // check list generation
        final var matchListGenerator = new StandardMatchListGenerator();
        final var matchList = matchListGenerator.generate(set1);
        assertEquals(24, matchList.size());

        // Fake results
        final Set<Team> winTeams = new HashSet<>();
        matchList.forEach(mm -> {
            final var m = mm.getMatch();
            if (winTeams.contains(m.getTeamOne())) {
                m.setWinner(Match.Winner.ONE);
            } else if (winTeams.contains(m.getTeamTwo())) {
                m.setWinner(Match.Winner.TWO);
            } else {
                m.setWinner(Match.Winner.ONE);
                winTeams.add(m.getTeamOne());
            }
        });

        assertTrue(set1.isComplete());
        assertTrue(round.isComplete());


        log.info("RACELIST %s".formatted(set1.name()));
        for (int i = 0; i < matchList.size(); i++) {
            final var mm = matchList.get(i);
            final var match = mm.getMatch();
            log.info("{} {} {} : {} v {} ({})", mm.getDivision().name(), mm.getMinileague(), i + 1, match.getTeamOne().name(), match.getTeamTwo().name(), match.getWinner().name());
        }

        log.info("SET 1 RESULTS");
        final MatchResultProcessor mrp = new BasicMatchResultProcessor();
        for (Round division : set1divisions) {
            assertTrue(division.isComplete());
            for (Round minileague : division.subRounds()) {
                assertTrue(minileague.isComplete());
                final String msg = "%s->%s".formatted(division.name(), minileague.name());
                final List<Team> teamResults = mrp.getResults(minileague.matches());
                teamResults.forEach(t -> log.info("{}->{}", msg, t.name()));
            }
        }

        // FIXME pretty sure these are ignored
        new SetTwoMatchGenerator(2).generate(round);
        log.info("ROUND SET 2 DEBUG\n{}", round.subRounds().get(1).debug());

        assertEquals(2, round.subRounds().size());
        // check set 2
        final var set2 = round.subRounds().get(1);
        assertEquals(24, set2.matches().size());

        // check each division in set 2
        assertEquals(2, set2.subRounds().size());
        final var set2div1 = set2.subRounds().get(0);
        assertEquals(2, set2div1.subRounds().size());
        final var set2div2 = set2.subRounds().get(1);
        assertEquals(2, set2div2.subRounds().size());

        // check each minileague in each division in set 2
        final var set2div1ml1 = set2div1.subRounds().get(0);
        assertEquals(3, set2div1ml1.subRounds().size());
        final var set2div1ml2 = set2div1.subRounds().get(1);
        assertEquals(3, set2div1ml2.subRounds().size());
        final var set2div2ml1 = set2div2.subRounds().get(0);
        assertEquals(3, set2div2ml1.subRounds().size());
        final var set2div2ml2 = set2div2.subRounds().get(1);
        assertEquals(3, set2div2ml2.subRounds().size());

        final var matchList2 = matchListGenerator.generate(set2);
        assertEquals(24, matchList2.size());

        // Fake results
        final Set<Team> winTeams2 = new HashSet<>();
        matchList2.forEach(mm -> {
            final var m = mm.getMatch();
            if (winTeams2.contains(m.getTeamOne())) {
                m.setWinner(Match.Winner.ONE);
            } else if (winTeams2.contains(m.getTeamTwo())) {
                m.setWinner(Match.Winner.TWO);
            } else {
                m.setWinner(Match.Winner.ONE);
                winTeams2.add(m.getTeamOne());
            }
        });

        log.info("RACELIST %s".formatted(set2.name()));
        for (int i = 0; i < matchList2.size(); i++) {
            final var mm = matchList2.get(i);
            final var match = mm.getMatch();
            log.info("{} {} {} : {} v {} ({})", mm.getDivision().name(), mm.getMinileague(), i + 1, match.getTeamOne().name(), match.getTeamTwo().name(), match.getWinner().name());
        }

        assertTrue(set2.isComplete());
        assertTrue(round.isComplete());

        log.info("SET 2 RESULTS");
        final var set2divisions = set2.subRounds();
        for (Round division : set2divisions) {
            assertTrue(division.isComplete());
            for (Round minileague : division.subRounds()) {
                assertTrue(minileague.isComplete());
                final String msg = "%s->%s".formatted(division.name(), minileague.name());
                final List<Team> teamResults = mrp.getResults(minileague.matches());
                teamResults.forEach(t -> log.info("{}->{}", msg, t.name()));
            }
        }

        // Prove we can serialise everything normalised...

        // Check serialization normalisation manually
        final ObjectMapper normaliser = JacksonSerializationUtils.normalisedRoundMapper();
        log.info(normaliser.writerWithDefaultPrettyPrinter().writeValueAsString(round));
        // TODO check deserialization

        final ObjectMapper om = JacksonSerializationUtils.normalisedMapper();
        final Function<Collection<?>, String> stringer = (Collection<?> items) -> {
            try {
                return om.writerWithDefaultPrettyPrinter().writeValueAsString(items.stream()
                        .collect(Collectors.toMap(Function.identity(), Function.identity())));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Could not serialise JSON", e);
            }
        };

        log.info("\n{}", stringer.apply(registry.getLeagues()));
        log.info("\n{}", stringer.apply(registry.getDivisions()));
        log.info("\n{}", stringer.apply(registry.getTeams()));
        log.info("\n{}", stringer.apply(registry.getClubs()));
    }
}
