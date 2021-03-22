package net.aflb.kaas.core.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;

import java.io.IOException;
import java.util.function.Function;

public class SerializationUtils {

    private SerializationUtils() {}

    /**
     * Returns an {@link ObjectMapper} which will normalise a {@link Round}
     * @return an {@link ObjectMapper} for normalising kaas models
     */
    public static ObjectMapper normalisedRoundMapper() {
        final var om = baseMapper();
        final var mod = baseModule();
        mod.addSerializer(Team.class, SerializationUtils.normalizer(t -> t.id().id()));
        mod.addSerializer(Club.class, SerializationUtils.normalizer(c -> c.id().id()));
        mod.addSerializer(Division.class, SerializationUtils.normalizer(d -> d.id().id()));
        mod.addSerializer(Match.class, SerializationUtils.normalizer(m -> m.getId().id()));
        mod.addSerializer(League.class, SerializationUtils.normalizer(l -> l.id().id()));
        om.registerModule(mod);

        return om;
    }

    /**
     * Returns an {@link ObjectMapper} which will approximately normalise a {@link Match}
     * @return an {@link ObjectMapper} for normalising kaas models
     */
    public static ObjectMapper normalisedMatchMapper() {
        final var om = baseMapper();
        final var mod = baseModule();
        mod.addSerializer(Team.class, SerializationUtils.normalizer(t -> t.id().id()));
        om.registerModule(mod);

        return om;
    }

    /**
     * Returns an {@link ObjectMapper} which will fully normalise a {@link Round} and more generally the kaas data
     * models.
     *
     * @return an {@link ObjectMapper} for normalising kaas models
     */
    public static ObjectMapper normalisedMapper() {
        final var om = baseMapper();
        final var mod = baseModule();
        om.registerModule(mod);

        return om;
    }

    private static ObjectMapper baseMapper() {
        final var om = new ObjectMapper().deactivateDefaultTyping();

        om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        return om;
    }

    /**
     * Returns a {@link SimpleModule} with key serialisers for all entities. i.e. an entity appearing as a key will be
     * serialized to it's ID
     * @return a {@link SimpleModule}
     */
    private static SimpleModule baseModule() {
        final var sm = new SimpleModule();

        sm.addKeySerializer(Team.class, SerializationUtils.keyNormalizer(t -> t.id().id()));
        sm.addKeySerializer(Club.class, SerializationUtils.keyNormalizer(c -> c.id().id()));
        sm.addKeySerializer(Division.class, SerializationUtils.keyNormalizer(d -> d.id().id()));
        sm.addKeySerializer(Match.class, SerializationUtils.keyNormalizer(m -> m.getId().id()));
        sm.addKeySerializer(Round.class, SerializationUtils.keyNormalizer(r -> r.id().id()));
        sm.addKeySerializer(League.class, SerializationUtils.keyNormalizer(l -> l.id().id()));

        return sm;
    }

    public static <T> StdSerializer<T> keyNormalizer(final Function<T, String> idGetter) {
        return new KeyIdSerializer<>(idGetter);
    }

    public static <T> StdSerializer<T> normalizer(final Function<T, String> idGetter) {
        return new IdSerializer<>(idGetter);
    }

    private static class KeyIdSerializer<T> extends StdSerializer<T> {
        private final Function<T, String> idGetter;

        public KeyIdSerializer(final Function<T, String> idGetter) {
            this(null, idGetter);
        }

        public KeyIdSerializer(Class<T> t, Function<T, String> idGetter) {
            super(t);
            this.idGetter = idGetter;
        }

        @Override
        public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeFieldName(idGetter.apply(t));
        }
    }

    private static class IdSerializer<T> extends StdSerializer<T> {
        private final Function<T, String> idGetter;

        public IdSerializer(final Function<T, String> idGetter) {
            this(null, idGetter);
        }

        public IdSerializer(Class<T> t, Function<T, String> idGetter) {
            super(t);
            this.idGetter = idGetter;
        }

        @Override
        public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(idGetter.apply(t));
        }
    }
}