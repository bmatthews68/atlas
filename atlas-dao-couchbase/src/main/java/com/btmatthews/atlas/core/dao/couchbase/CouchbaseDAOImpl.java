package com.btmatthews.atlas.core.dao.couchbase;

import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.dao.DAO;
import com.couchbase.client.CouchbaseClientIF;
import net.spy.memcached.transcoders.Transcoder;

import java.util.List;

public class CouchbaseDAOImpl<ID, I> implements DAO<ID, I> {

    private final CouchbaseClientIF client;

    private final String prefix;

    private final Transcoder<I> transcoder;

    public CouchbaseDAOImpl(final CouchbaseClientIF client,
                            final String prefix,
                            final Transcoder<I> transcoder) {
        this.client = client;
        this.prefix = prefix;
        this.transcoder = transcoder;
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
        client.add(buildKey(id), 0, obj, transcoder);
    }

    @Override
    public I read(final ID id) {
        return client.get(buildKey(id), transcoder);
    }

    @Override
    public List<I> read(final ID... ids) {
        return null;
    }

    @Override
    public void update(final ID id, final I obj) {
        client.set(buildKey(id), 0, obj, transcoder);
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
}
