package com.btmatthews.atlas.core.domain.jsr310;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Created by bmatthews68 on 23/03/2014.
 */
public class TestLocalDateTimeSerialization {
    private static final String TEST_JSON = "{\"dateTime\":{\"$date\":\"1999-01-01T00:00:00.000Z\"}}";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializer() throws IOException {
        final ObjectWithTime obj = objectMapper.readValue(TEST_JSON, ObjectWithTime.class);
        assertEquals(obj.getDateTime(), LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0));
    }

    @Test
    public void testSerializer() throws JsonProcessingException {
        final ObjectWithTime obj = new ObjectWithTime(LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0));
        final String json = objectMapper.writeValueAsString(obj);
        assertEquals(TEST_JSON, json);
    }

}
