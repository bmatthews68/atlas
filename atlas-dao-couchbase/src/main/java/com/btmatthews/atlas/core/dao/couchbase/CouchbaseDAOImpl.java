package com.btmatthews.atlas.core.dao.couchbase;

import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.dao.DAO;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

public class CouchbaseDAOImpl<ID, I, T extends I> implements DAO<ID, I> {

    private final CouchbaseClient client;

    private final ObjectMapper objectMapper;

    private final String prefix;

    private final Class<I> objectClass;

    public CouchbaseDAOImpl(final CouchbaseClient client,
                            final ObjectMapper objectMapper,
                            final String prefix,
                            final Class<I> objectClass) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.prefix = prefix;
        this.objectClass = objectClass;
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<I> find(final Paging paging) {
        throw new UnsupportedOperationException();
    }

    @Override
    public I lookup(final String key,
                    final Object value) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void create(final ID id, final I obj) {
        client.add(buildKey(id), 0, obj, new JsonTranscoder());
    }

    @Override
    public I read(final ID id) {
        return client.get(buildKey(id), new JsonTranscoder());
    }

    @Override
    public List<I> read(final ID... ids) {
        return null;
    }

    @Override
    public void update(final ID id, final I obj) {
        client.set(buildKey(id), 0, obj, new JsonTranscoder());
    }

    @Override
    public void destroy(final ID id) {
        client.delete(buildKey(id));
    }

    private String buildKey(final ID id) {
        final StringBuilder builder = new StringBuilder(prefix);
        builder.append("::");
        builder.append(id.toString());
        return builder.toString();
    }

    private class JsonTranscoder implements Transcoder<I> {
        @Override
        public boolean asyncDecode(final CachedData cachedData) {
            return false;
        }

        @Override
        public CachedData encode(final I o) {
            try {
                final String json = objectMapper.writeValueAsString(o);
                final byte[] data = json.getBytes(Charsets.UTF_8);
                return new CachedData(0, data, data.length);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        @Override
        public I decode(final CachedData cachedData) {
            try {
                final String json = new String(cachedData.getData(), Charsets.UTF_8);
                return objectMapper.readValue(json, objectClass);
            } catch (final IOException e) {
                return null;
            }
        }

        @Override
        public int getMaxSize() {
            return CachedData.MAX_SIZE;
        }
    }
}
