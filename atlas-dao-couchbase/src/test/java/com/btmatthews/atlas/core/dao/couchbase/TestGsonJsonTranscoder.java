package com.btmatthews.atlas.core.dao.couchbase;

import net.spy.memcached.transcoders.Transcoder;

public class TestGsonJsonTranscoder extends AbstractTestJsonTranscoder {

    @Override
    protected Transcoder<Person> getPersonTranscoder() {
        return new GsonJsonTranscoder<>(PersonImpl.class);
    }
}
