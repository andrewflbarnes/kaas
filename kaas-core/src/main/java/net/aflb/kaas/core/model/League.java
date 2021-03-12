package net.aflb.kaas.core.model;

import net.aflb.kaas.core.KaasID;

import java.util.Locale;
import java.util.function.Supplier;

public record League(
        KaasID id,
        String name
) implements Comparable<League> {
    private static final Supplier<KaasID> GEN = KaasID.generator(League.class);

    public static League of(final String name) {
        // FIXME
        return new League(GEN.get(), name);
    }

    @Override
    public int compareTo(League o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
