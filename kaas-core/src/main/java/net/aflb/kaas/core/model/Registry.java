package net.aflb.kaas.core.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// TODO make threadsafe
public class Registry {
    final Map<League, Map<Club, Set<Team>>> leagues = new HashMap<>();
    final Map<Division, Set<Team>> divisions = new HashMap<>();

    public List<League> getLeagues() {
        return new ArrayList<>(leagues.keySet());
    }

    public List<Division> getDivisions() {
        return new ArrayList<>(divisions.keySet());
    }

    public List<Club> getClubs() {
        return leagues.values().stream()
                .map(Map::keySet)
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    public List<Team> getTeams() {
        return divisions.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    public Registry registerLeague(final League league) {
        if (leagues.containsKey(league)) {
            throw new IllegalArgumentException("League %s already registered"
                    .formatted(league.name()));
        }
        leagues.put(league, new HashMap<>());
        return this;
    }

    public Registry registerDivision(final Division division) {
        if (divisions.containsKey(division)) {
            throw new IllegalArgumentException("Division %s already registered"
                    .formatted(division.name()));
        }
        divisions.put(division, new HashSet<>());
        return this;
    }

    public Registry registerClub(final Club club, final League league) {
        final var leagueClubs = leagues.get(league);
        if (leagueClubs == null) {
            throw new IllegalArgumentException("Cannot add club %s as league %s is not registered"
                    .formatted(club.name(), league.name()));
        }
        if (leagueClubs.containsKey(club)) {
            throw new IllegalArgumentException("Club %s already registered"
                    .formatted(club.name()));
        }
        leagueClubs.put(club, new HashSet<>());
        return this;
    }

    public Registry registerTeam(final Team team, final Club club, final Division division) {
        final var clubs = clubs();
        if (!clubs.containsKey(club)) {
            throw new IllegalArgumentException("Cannot add team %s as club %s is not registered"
                    .formatted(club.name(), club.name()));
        }
        if (!divisions.containsKey(division)) {
            throw new IllegalArgumentException("Cannot add team %s as division %s is not registered"
                    .formatted(club.name(), division.name()));
        }
        divisions.get(division).add(team);
        clubs.get(club).add(team);
        return this;
    }

    private Map<Club, Set<Team>> clubs() {
        return leagues.values().stream()
                .flatMap(clubs -> clubs.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<League, Map<Division, List<Team>>> getSeeds(final Comparator<Team> strategy) {
        final var result = new HashMap<League, Map<Division, List<Team>>>();
        final var clubs = clubs();
        leagues.forEach((l, cs) -> {
            final var teams = clubs.entrySet().stream()
                    .filter(e -> cs.containsKey(e.getKey()))
                    .map(Map.Entry::getValue)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
            final var lMap = new HashMap<Division, List<Team>>();
            result.put(l, lMap);
            divisions.forEach((d, ts) -> {
                final var teamList = ts.stream()
                        .filter(teams::contains)
                        .sorted(strategy)
                        .collect(Collectors.toList());
                lMap.put(d, teamList);
            });
        });

        return result;
    }
}
