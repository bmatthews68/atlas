package com.btmatthews.atlas.core.dao.couchbase;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import net.spy.memcached.CachedData;

public final class GsonJsonTranscoder<I> extends AbstractJsonTranscoder<I> {

    public GsonJsonTranscoder(final Class<? extends I> objectClass) {
        super(objectClass);
    }

    @Override
    public CachedData encode(final I o) {
        final Gson gson = new Gson();
        final String json = gson.toJson(o);
        final byte[] data = json.getBytes(Charsets.UTF_8);
        return new CachedData(0, data, data.length);
    }

    @Override
    public I decode(final CachedData cachedData) {
        final Gson gson = new Gson();
        final String json = new String(cachedData.getData(), Charsets.UTF_8);
        return gson.fromJson(json, getObjectClass());
    }
}
