package net.aflb.kaas.core.model;

import net.aflb.kaas.core.KaasID;

import java.util.Comparator;
import java.util.Locale;
import java.util.function.Supplier;

public record Division(
        KaasID id,
        String name,
        int rank
) implements Comparable<Division> {
    private static final Supplier<KaasID> GEN = KaasID.generator(Division.class);
    public static final Comparator<Division> BY_RANK = Comparator.comparingInt(Division::rank);
    public static final Division NONE = Division.of("NONE", 999);

    public static Division of(final String name, final int rank) {
        // FIXME
        return new Division(GEN.get(), name, rank);
    }

    @Override
    public int compareTo(Division o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
