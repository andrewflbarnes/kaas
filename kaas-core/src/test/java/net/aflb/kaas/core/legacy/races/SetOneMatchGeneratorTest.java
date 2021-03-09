package net.aflb.kaas.core.legacy.races;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.Registry;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.kings.engine.SetOneMatchGenerator;
import net.aflb.kaas.kings.engine.StandardMatchListGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SetOneMatchGeneratorTest {

    @Test
    void test() {
        final Registry registry = new Registry();
        final League league = League.of("league");
        final Division division1 = Division.of("division1",1);
        final Division division2 = Division.of("division2", 2);
        final Club club1 = Club.of("Club 1");
        final Club club2 = Club.of("Club 2");
        final Club club3 = Club.of("Club 3");
        final Club club4 = Club.of("Club 4");
        final Club club5 = Club.of("Club 5");
        final Club club6 = Club.of("Club 6");
        Team team11 = Team.of("team 1 1");
        Team team21 = Team.of("team 2 1");
        Team team12 = Team.of("team 1 2");
        Team team13 = Team.of("team 1 3");
        Team team14 = Team.of("team 1 4");
        Team team22 = Team.of("team 2 2");
        Team team23 = Team.of("team 2 3");
        Team team24 = Team.of("team 2 4");
        Team team31 = Team.of("team 3 1");
        Team team32 = Team.of("team 3 2");
        Team team33 = Team.of("team 3 3");
        Team team41 = Team.of("team 4 1");
        Team team51 = Team.of("team 5 1");
        Team team61 = Team.of("team 6 1");
        Team team62 = Team.of("team 6 2");
        Team team63 = Team.of("team 6 3");
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
                .registerTeam(team21, club2, division1)
                .registerTeam(team12, club1, division1)
                .registerTeam(team13, club1, division1)
                .registerTeam(team14, club1, division1)
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

        log.info("{}", round.debug());
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

        final var set = sets.get(0);

        final var divisions = set.subRounds();
        assertNotNull(divisions);
        assertEquals(2, divisions.size());

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
        divisions.forEach(d -> d.teamRankings().values().forEach(ts -> {
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
        final var matchList = matchListGenerator.generate(set);
        assertEquals(24, matchList.size());

        log.info("RACELIST %s".formatted(set.name()));
        matchList.forEach(match ->
                log.info("{} : {} v {} {}", match.getKassId(), match.getTeamOne().name(), match.getTeamTwo().name(), match.getWinner()));
    }

}
