package com.btmatthews.atlas.core.domain.jsr310;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TestLocalDateTimeSerialization {
    private static final String TEST_JSON1 = "{\"dateTime\":{\"$date\":\"1999-01-01T00:00:00.000Z\"}}";
    private static final String TEST_JSON2 = "{\"dateTime\":{\"$date\":\"1786-08-08T00:00:00.000Z\"}}";
    private static final String TEST_JSON3 = "{\"dateTime\":{\"$date\":\"9999-12-31T00:00:00.000Z\"}}";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializer() throws IOException {
        ObjectWithTime obj = objectMapper.readValue(TEST_JSON1, ObjectWithTime.class);
        assertEquals(obj.getDateTime(), LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0));
        obj = objectMapper.readValue(TEST_JSON2, ObjectWithTime.class);
        assertEquals(obj.getDateTime(), LocalDateTime.of(1786, 8, 8, 0, 0, 0, 0));
        obj = objectMapper.readValue(TEST_JSON3, ObjectWithTime.class);
        assertEquals(obj.getDateTime(), LocalDateTime.of(9999, 12, 31, 0, 0, 0, 0));
    }

    @Test
    public void testSerializer() throws JsonProcessingException {
        ObjectWithTime obj = new ObjectWithTime(LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0));
        String json = objectMapper.writeValueAsString(obj);
        assertEquals(TEST_JSON1, json);
        obj = new ObjectWithTime(LocalDateTime.of(1786, 8, 8, 0, 0, 0, 0));
        json = objectMapper.writeValueAsString(obj);
        assertEquals(TEST_JSON2, json);
        obj = new ObjectWithTime(LocalDateTime.of(9999, 12, 31, 0, 0, 0, 0));
        json = objectMapper.writeValueAsString(obj);
        assertEquals(TEST_JSON3, json);
    }

}
