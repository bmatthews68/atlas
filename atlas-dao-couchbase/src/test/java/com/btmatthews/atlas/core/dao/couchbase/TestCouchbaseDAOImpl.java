package com.btmatthews.atlas.core.dao.couchbase;

import com.btmatthews.atlas.core.dao.DAO;
import com.couchbase.client.CouchbaseClientIF;
import net.spy.memcached.transcoders.Transcoder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestCouchbaseDAOImpl {

    @Mock
    private CouchbaseClientIF client;
    @Mock
    private Transcoder<Person> transcoder;
    private DAO<String, Person> personDao;

    @Before
    public void setup() {
        initMocks(this);
        personDao = new CouchbaseDAOImpl<>(client, "person", transcoder);
    }

    @Test
    public void createNewPerson() {
        final Person person = new PersonImpl(PersonTestData.PERSON_ID, PersonTestData.PERSON_NAME, PersonTestData.PERSON_EMAIL);
        personDao.create(PersonTestData.PERSON_ID, person);
        verify(client).add(
                eq("person::" + PersonTestData.PERSON_ID),
                eq(0),
                same(person),
                any(Transcoder.class));
        verifyNoMoreInteractions(client, transcoder);
    }
}
