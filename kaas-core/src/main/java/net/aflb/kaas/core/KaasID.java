package net.aflb.kaas.core;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public record KaasID(
        String id
) {
    private static final Set<String> REGISTERED = new HashSet<>();

    /**
     * This is intended to be used in static initializers.
     *
     * @param prefix the prefix for the ID
     * @return an ID generator with IDs in the form {@code <prefix>{<UUID>}}
     */
    public static Supplier<KaasID> generator(final String prefix) {
        synchronized (REGISTERED) {
            if (REGISTERED.contains(prefix)) {
                throw new ExceptionInInitializerError("%s is already registered for ID generation".formatted(prefix));
            }
            REGISTERED.add(prefix);
            return () -> new KaasID("%s{%s}".formatted(prefix, UUID.randomUUID().toString()));
        }
    }

    /**
     * This is intended to be used in static initializers.
     *
     * @param clazz the class to use as the ID prefix
     * @return an ID generator with IDs in the form {@code <class name>{<UUID>}}
     */
    public static Supplier<KaasID> generator(final Class<?> clazz) {
        return generator(clazz.getSimpleName());
    }
}
