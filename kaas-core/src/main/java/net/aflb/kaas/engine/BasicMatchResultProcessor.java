package net.aflb.kaas.engine;

import lombok.RequiredArgsConstructor;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.spi.ManualInterventionException;
import net.aflb.kaas.core.spi.MatchResultProcessor;
import net.aflb.kaas.core.spi.RaceNotFoundException;
import net.aflb.kaas.core.spi.RaceNotRunException;
import net.aflb.kaas.core.spi.RacesUnfinishedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicMatchResultProcessor implements MatchResultProcessor {

    private static final int WEIGHT_WIN = 100;
    private static final int WEIGHT_DSQ = 10;
    private static final int WEIGHT_ADJUSTMENT = 1;

    @RequiredArgsConstructor
    private static class WinDsq {
        private final Team team;
        private int wins = 0;
        private int dsqs = 0;
        private int adjustments = 0;

        int weighting() {
            return WEIGHT_WIN * wins - WEIGHT_DSQ * dsqs + WEIGHT_ADJUSTMENT * adjustments;
        }
    }

    // TODO get from apache commons or similar
    private static boolean isEmpty(final String test) {
        return test == null || test.isBlank();
    }

    @Override
    public List<Team> getResults(final List<Match<?>> matches)
        // TODO return enum instead of throw exception?
            throws ManualInterventionException, RacesUnfinishedException {
        if (matches.stream().anyMatch(m -> !m.isComplete())) {
            throw new RacesUnfinishedException("There are still incomplete races");

        }
        final var teamOrder = getTeamWinsAndDsqs(matches);

        // pass one - we have a built in weighting which we sort on giving approximate team standings
        final var passOne = teamOrder.values().stream()
                .sorted(Comparator.comparingInt(WinDsq::weighting))
                .collect(Collectors.toList());

        // Second pass: traverse through the array and find drawing teams
        // -two drawing teams, single race:
        // ---see who won the race
        // -two drawing teams, 2 races:
        // ---see who won the races, if drawn require a rerun
        // -three drawing teams, single races (FIXME):
        // ---technically we need to rerun all these, but we can't risk a
        // ---repeat result (as it could continue forever. Instead we take the
        // ---highest seeded team as first and then check who won the race
        // ---between the two lower teams.
        // -three drawing teams, 2 races (FIXME):
        // ---Why the fuck do you even have a division with 4 teams in it?
        // ---Anyway, as above take the highest seeded, then compare the
        // ---lower teams. If they have drawn require a rerun.
        // -four drawing teams (surprisingly, possible in a group of 6) (TODO):
        // ---this is too complex for my poor brain. Basically you can
        // ---take into account of how races played out between these 4
        // ---teams with the undrawn teams to determine who is better/worse
        // ---splitting them into a drawn group of 3 and 1 or two drawn groups
        // ---of two.
        // ---Yeah so, I'm not coding the 4 case draw for shit.
        // ---Lob in an exception for this, relay info to user and telll them to
        // ---"massage" the figures.

        final var passTwo = new ArrayList<WinDsq>(passOne.size());
        final List<Team> seedCheck = new ArrayList<>(3);
        for (int i = 0, n = passOne.size(), idx = 0; i < n; i = idx) {
            final var current = passOne.get(i);
            passTwo.add(current);
            final var currentWeighting = current.weighting();

            while (idx < n - 1 && currentWeighting == passOne.get(idx + 1).weighting()) {
                // Add teams if they have the same weighting
                idx++;
                passTwo.add(passOne.get(idx));
            }

            // We now have idx - i + 1 elements in passTwo with matching weightings
            int numDrawnTeams = idx - i + 1;
            //TODO
//			switch (numDrawnTeams) {
//				case 1:
//					continue;
//				case 2:
//				case 3:
//				default:
//			}
//			idx++;
            if (numDrawnTeams == 2) {
                int whoWon;
                try {
                    whoWon = whoWon(matches, passTwo.get(i), passTwo.get(i + 1));
                } catch (RaceNotRunException e) {
                    throw new RacesUnfinishedException(e);
                }
                if (whoWon == 0) {
                    throw new ManualInterventionException("tiebrreaker required");
                    // TODO we need to run a tiebreaker race (or hacky set by seeding which isn't recommended)
                }
            } else if (numDrawnTeams == 3) {
                throw new ManualInterventionException(numDrawnTeams + " team draw, massage required");
                // TODO
//                final List<Team> seedCheck = new ArrayList<>(3);
//                seedCheck.clear();
//                for (int j = i; j < i + 3; j++) {
//                    seedCheck.add(passTwo[j]);
//                }
//
//                // Get the highest seed then the next two arbitrarily
//                Collections.sort(seedCheck);
//                passTwo[i] = seedCheck.get(0);
//                passTwo[i + 1] = seedCheck.get(1);
//                passTwo[i + 2] = seedCheck.get(2);
//
//                // See who won of the other two
//                int whoWon;
//                try {
//                    whoWon = whoWon(passTwo[i + 1], passTwo[i + 2]);
//                } catch (RaceNotRunException e) {
//                    throw new RacesUnfinishedException(e);
//                }
//
//                // Use teamOne as a temporary variable
//                // Note: if whoWon > 0 then the teams are already in the correct order
//                if (whoWon < 0) {
//                    tempTeam = passTwo[i + 1];
//                    passTwo[i + 1] = passOne[i + 2];
//                    passTwo[i + 2] = tempTeam;
//                } else if (whoWon == 0 && passTwo[i + 1].compareTo(passTwo[i + 2]) > 0) {
//                    // TODO Add rerace functionality here instead of seeding
//                    tempTeam = passTwo[i + 1];
//                    passTwo[i + 1] = passOne[i + 2];
//                    passTwo[i + 2] = tempTeam;
//                }
            } else if (numDrawnTeams > 3) {
                // More than 3 drawn teams throw exception
                throw new ManualInterventionException(numDrawnTeams + " team draw, massage required");
            }

            idx++;
        }

        return passTwo.stream()
                .map(wd -> wd.team)
                .collect(Collectors.toList());
    }

    private Map<Team, WinDsq> getTeamWinsAndDsqs(final List<Match<?>> matches) {
        final Map<Team, WinDsq> teamWinsAndDsqs = new HashMap<>(6);

        // FIXME - what if we're not complete? NPE central!
        matches.forEach(match -> {
            final Team teamOne = match.getTeamOne();
            final Team teamTwo = match.getTeamTwo();

            // init if not yet added
            teamWinsAndDsqs.computeIfAbsent(teamOne, WinDsq::new);
            teamWinsAndDsqs.computeIfAbsent(teamTwo, WinDsq::new);

            // Update wins
            final var winner = match.getWinner();
            if (winner != null) {
                teamWinsAndDsqs.get(winner).wins += 1;
            }

            // Update the DSQs if appropriate
            if (!isEmpty(match.getTeamOneDsq())) {
                teamWinsAndDsqs.get(teamOne).dsqs += 1;
            }
            if (!isEmpty(match.getTeamTwoDsq())) {
                teamWinsAndDsqs.get(teamTwo).dsqs += 1;
            }

        });

        return teamWinsAndDsqs;
    }

    /**
     * <p>
     * Given two team IDs, returns the difference between the number of races
     * won by each. In the case where a race has not been raced, a
     * {@link RaceNotRunException} is thrown.
     * </p>
     *
     * <p>
     * Note that in some group sizes teams will race each other team more than
     * once and hence values are not limited to -1. 0 and 1. In the case
     * </p>
     *
     * @param wdOne the {@link WinDsq} of the first competing team
     * @param wdTwo the {@link WinDsq} of the second competing team
     * @return positive value if teamOne won, negative value if teamTwo won, 0
     * if they one the same number of races against each other (is this possible???)
     * @throws RaceNotRunException if there is a race between the two teams which has yet to be
     *                             completed
     */
    private int whoWon(final List<Match<?>> matches, final WinDsq wdOne, final WinDsq wdTwo) throws RaceNotRunException {
        final var teamOne = wdOne.team;
        final var teamTwo = wdTwo.team;
        // We keep track of the return value as in some cases teams can race
        // each other multiple times in a single set
        int retval = 0;
        int racesFound = 0;
        int unrunRaces = 0;
        for (Match<?> match : matches) {
            if (teamOne.equals(match.getTeamOne()) && teamTwo.equals(match.getTeamTwo())) {
                if (teamOne.equals(match.getWinner())) {
                    retval += WEIGHT_WIN;
                } else if (teamTwo.equals(match.getWinner())) {
                    retval -= WEIGHT_WIN;
                } else {
                    unrunRaces += 1;
                }
                if (!isEmpty(match.getTeamOneDsq())) {
                    retval -= WEIGHT_DSQ;
                }
                if (!isEmpty(match.getTeamTwoDsq())) {
                    retval += WEIGHT_DSQ;
                }
                racesFound += 1;
            } else if (teamTwo.equals(match.getTeamOne()) && teamOne.equals(match.getTeamTwo())) {
                if (teamOne.equals(match.getWinner())) {
                    retval -= WEIGHT_WIN;
                } else if (teamTwo.equals(match.getWinner())) {
                    retval += WEIGHT_WIN;
                } else {
                    unrunRaces += 1;
                }
                if (!isEmpty(match.getTeamOneDsq())) {
                    retval += WEIGHT_DSQ;
                }
                if (!isEmpty(match.getTeamTwoDsq())) {
                    retval -= WEIGHT_DSQ;
                }
                racesFound += 1;
            }
        }

        final var teamOneId = teamOne.name();
        final var teamTwoId = teamTwo.name();
        // If the race doesn't exist throw an unchecked exception - the way we
        // currently run races, all teams in a group compete against each other.
        if (racesFound == 0) {
            throw new RaceNotFoundException("Race for team [" + teamOneId
                    + "] and team [" + teamTwoId + "] not found");
        }

        if (unrunRaces > 0) {
            throw new RaceNotRunException(unrunRaces + " unrun races for team [" + teamOneId
                    + "] and team [" + teamTwoId + "] not found");
        }

        if (retval > 0) {
            wdOne.adjustments = 1;
            wdOne.adjustments = 0;
        } else if (retval < 0) {
            wdOne.adjustments = 0;
            wdOne.adjustments = 1;
        }

        return retval;
    }
}
