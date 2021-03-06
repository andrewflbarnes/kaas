package net.aflb.kaas.core.model;

public record Seeding(
        Team team,
        long rank
) implements Comparable<Seeding> {
    @Override
    public int compareTo(Seeding o) {
        return (int)(rank - o.rank);
    }
}
