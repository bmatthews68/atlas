package com.btmatthews.atlas.core.dao.couchbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import net.spy.memcached.CachedData;

import java.io.IOException;

public class JacksonJsonTranscoder<I> extends AbstractJsonTranscoder<I> {

    private final ObjectMapper objectMapper;

    public JacksonJsonTranscoder(final Class<? extends I> objectClass,
                                 final ObjectMapper objectMapper) {
        super(objectClass);
        this.objectMapper = objectMapper;
    }

    @Override
    public CachedData encode(final I o) {
        try {
            final String json = objectMapper.writeValueAsString(o);
            final byte[] data = json.getBytes(Charsets.UTF_8);
            return new CachedData(0, data, data.length);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public I decode(final CachedData cachedData) {
        try {
            final String json = new String(cachedData.getData(), Charsets.UTF_8);
            return objectMapper.readValue(json, getObjectClass());
        } catch (final IOException e) {
            return null;
        }
    }
}
