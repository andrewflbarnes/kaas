package net.aflb.kaas.core.model;

import net.aflb.kaas.core.KaasID;

import java.util.Locale;
import java.util.function.Supplier;

public record Club(
        KaasID id,
        String name
) implements Comparable<Club> {
    private static final Supplier<KaasID> GEN = KaasID.generator(Club.class);

    public static Club of(final String name) {
        return new Club(GEN.get(), name);
    }
    @Override
    public int compareTo(Club o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
