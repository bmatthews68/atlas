package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 05/04/2013
 * Time: 08:41
 * To change this template use File | Settings | File Templates.
 */
public class TestPoolableSessionFactory {

    private final static String TEST_WORKSPACE_NAME = "default";

    private PoolableSessionFactory factory;
    @Mock
    private Repository repository;
    @Mock
    private Credentials credentials;
    @Mock
    private CredentialsProvider credentialsProvider;

    @Before
    public void setup() {
        initMocks(this);
        when(credentialsProvider.getGlobalCredentials()).thenReturn(credentials);
        factory = new PoolableSessionFactory(repository, credentialsProvider);
    }

    @Test
    public void makeObjectIsSuccessful() throws Exception {
        final Session session = mock(Session.class);
        when(repository.login(any(Credentials.class), anyString())).thenReturn(session);
        assertSame(session, factory.makeObject(TEST_WORKSPACE_NAME));
        verify(credentialsProvider).getGlobalCredentials();
        verify(repository).login(same(credentials), eq(TEST_WORKSPACE_NAME));
        verifyNoMoreInteractions(repository, credentialsProvider);
        verifyZeroInteractions(session);
    }

    @Test
    public void validateObjectIsSuccessfulWhenSessionIsLive() throws Exception {
        final Session session = mock(Session.class);
        when(session.isLive()).thenReturn(true);
        assertTrue(factory.validateObject(TEST_WORKSPACE_NAME, session));
        verify(session).isLive();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void validateObjectIsSuccessfulWhenSessionIsNotLive() throws Exception {
        final Session session = mock(Session.class);
        when(session.isLive()).thenReturn(false);
        assertFalse(factory.validateObject(TEST_WORKSPACE_NAME, session));
        verify(session).isLive();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void destroyObjectIsSuccessful() throws Exception {
        final Session session = mock(Session.class);
        factory.destroyObject(TEST_WORKSPACE_NAME, session);
        verify(session).logout();
        verifyNoMoreInteractions(session);
    }
}
