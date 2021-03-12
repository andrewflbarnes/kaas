package net.aflb.kaas.core.model;

import net.aflb.kaas.core.KaasID;

import java.util.Locale;
import java.util.function.Supplier;

public record Team(
        KaasID id,
        String name
) implements Comparable<Team> {
    private static final Supplier<KaasID> GEN = KaasID.generator(Team.class);

    public static Team of(final String name) {
        return new Team(GEN.get(), name);
    }

    @Override
    public int compareTo(Team o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
