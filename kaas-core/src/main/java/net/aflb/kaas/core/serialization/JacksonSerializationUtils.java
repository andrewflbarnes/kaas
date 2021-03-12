package net.aflb.kaas.core.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;

import java.io.IOException;
import java.util.function.Function;

public class JacksonSerializationUtils {

    private JacksonSerializationUtils() {}


    public static ObjectMapper normalisedObjectMapper() {
        final ObjectMapper om = new ObjectMapper().deactivateDefaultTyping();

        om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        SimpleModule sm = new SimpleModule();
        sm.addSerializer(Team.class, JacksonSerializationUtils.normalizer(t -> t.id().id()));
        sm.addKeySerializer(Division.class, JacksonSerializationUtils.keyNormalizer(d -> d.id().id()));
        sm.addKeySerializer(Match.class, JacksonSerializationUtils.keyNormalizer(m -> m.getId().id()));
        sm.addKeySerializer(Round.class, JacksonSerializationUtils.keyNormalizer(r -> r.id().id()));
        sm.addKeySerializer(League.class, JacksonSerializationUtils.keyNormalizer(l -> l.id().id()));
        om.registerModule(sm);

        return om;
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