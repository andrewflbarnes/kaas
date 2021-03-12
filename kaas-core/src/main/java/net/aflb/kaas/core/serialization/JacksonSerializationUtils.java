package net.aflb.kaas.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.function.Function;

public class JacksonSerializationUtils {
    public static <T> StdSerializer<T> normalizer(final Function<T, String> idGetter) {
        return new IdSerializer<>(idGetter);
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
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", idGetter.apply(t));
            jsonGenerator.writeEndObject();
        }
    }
}