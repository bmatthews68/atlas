package com.btmatthews.atlas.core.dao.couchbase;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractTestJsonTranscoder {

    private Transcoder<Person> transcoder;

    @Before
    public void setup() {
        transcoder = getPersonTranscoder();
    }

    @Test
    public void encodeNull() throws Exception {
        final CachedData cachedData = transcoder.encode(null);
        assertNotNull(cachedData);
        assertNotNull(cachedData.getData());
       /* with(new ByteArrayInputStream(cachedData.getData()))
                .assertNull("$"); */
    }

    @Test
    public void encodePerson() throws Exception {
        final Person person = new PersonImpl(
                PersonTestData.PERSON_ID,
                PersonTestData.PERSON_NAME,
                PersonTestData.PERSON_EMAIL);
        final CachedData cachedData = transcoder.encode(person);
        assertNotNull(cachedData);
        assertNotNull(cachedData.getData());
        with(new ByteArrayInputStream(cachedData.getData()))
                .assertThat("$.id", equalTo(PersonTestData.PERSON_ID))
                .assertThat("$.name", equalTo(PersonTestData.PERSON_NAME))
                .assertThat("$.email", equalTo(PersonTestData.PERSON_EMAIL));
    }

    protected abstract Transcoder<Person> getPersonTranscoder();
}
