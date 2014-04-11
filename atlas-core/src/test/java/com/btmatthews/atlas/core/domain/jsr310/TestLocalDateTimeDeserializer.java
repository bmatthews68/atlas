package com.btmatthews.atlas.core.domain.jsr310;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * Created by bmatthews68 on 23/03/2014.
 */
public class TestLocalDateTimeDeserializer {
    private static final String TEST_JSON = "{ \"dateTime\": { \"$date\": \"1999-01-01T00:00:00.000Z\" } }";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDesrializer() throws IOException {
        final ObjectWithTime obj = objectMapper.readValue(TEST_JSON, ObjectWithTime.class);
        assertEquals(obj.getDateTime(), LocalDateTime.of(1999, 1, 1, 0, 0, 0, 0));
    }

}
