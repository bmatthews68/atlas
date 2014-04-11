package com.btmatthews.atlas.core.dao.couchbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.spy.memcached.transcoders.Transcoder;

public class TestJacksonJsonTranscoder extends AbstractTestJsonTranscoder {

    @Override
    protected Transcoder<Person> getPersonTranscoder() {
        final ObjectMapper objectMapper = new ObjectMapper();
        return new JacksonJsonTranscoder<>(PersonImpl.class, objectMapper);
    }
}
