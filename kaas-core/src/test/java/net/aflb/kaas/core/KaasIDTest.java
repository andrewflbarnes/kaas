package net.aflb.kaas.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KaasIDTest {

    private static final String ID_STRING = "KAAS ID STRING";
    private static final String ID_PAYLOAD = "\"%s\"".formatted(ID_STRING);
    private static final KaasID KAAS_ID = new KaasID(ID_STRING);
    private static final ObjectMapper OM = new ObjectMapper();

    @Test
    void jackson_serialize() throws Exception {
        final String actual = OM.writeValueAsString(KAAS_ID);

        assertEquals(ID_PAYLOAD, actual);
    }

    @Test
    void jackson_deserialize() throws Exception {
        final KaasID actual = OM.readValue(ID_PAYLOAD.getBytes(StandardCharsets.UTF_8), KaasID.class);

        assertEquals(KAAS_ID, actual);
    }
}