package com.btmatthews.atlas.core.dao.couchbase;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;

/**
 * Abstract base class for trans-coders that encode/decode objects to/from JSON strings.
 *
 * @param <I> The interface that describes the object types bening stored
 */
public abstract class AbstractJsonTranscoder<I> implements Transcoder<I> {

    /**
     * The implementation class type.
     */
    private final Class<? extends I> objectClass;

    /**
     * Initialize the trans-coder
     * @param objectClass
     */
    protected AbstractJsonTranscoder(final Class<? extends I> objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * Get the implementation class type.
     *
     * @return The implementation class type.
     */
    protected final Class<? extends I> getObjectClass() {
        return objectClass;
    }

    @Override
    public final boolean asyncDecode(final CachedData cachedData) {
        return false;
    }

    @Override
    public final int getMaxSize() {
        return CachedData.MAX_SIZE;
    }
}
