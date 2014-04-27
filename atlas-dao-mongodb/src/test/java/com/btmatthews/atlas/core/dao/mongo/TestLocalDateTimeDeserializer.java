package com.btmatthews.atlas.core.dao.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TestLocalDateTimeDeserializer {
    private static final String TEST_JSON = "{ \"dateTime\": { \"$date\": \"1999-01-01T00:00:00.000Z\" } }";

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new MongoJSR310Module());
    }

    @Test
    public void testDeserializer() throws IOException {
        final ObjectWithTime obj = objectMapper.readValue(TEST_JSON, ObjectWithTime.class);
        assertEquals(obj.getDateTime(), LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0));
    }

}
