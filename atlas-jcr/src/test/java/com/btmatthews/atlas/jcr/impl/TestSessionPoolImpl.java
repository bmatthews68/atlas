package com.btmatthews.atlas.jcr.impl;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Session;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestSessionPoolImpl {

    private static final String DEFAULT_WORKSPACE_NAME = "default";
    @Mock
    private KeyedPoolableObjectFactory<String, Session> poolableSessionFactory;
    @Mock
    private Session pooledSession;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void borrowAndReturnASingleSession() throws Exception {
        final PooledSessionFactory sessionPool = new PooledSessionFactory(poolableSessionFactory);
        when(poolableSessionFactory.makeObject(DEFAULT_WORKSPACE_NAME)).thenReturn(pooledSession);
        final Session session = sessionPool.getSession(DEFAULT_WORKSPACE_NAME);
        assertSame(pooledSession, session);
        sessionPool.releaseSession(DEFAULT_WORKSPACE_NAME, session);
        sessionPool.shutdown();
        verify(poolableSessionFactory).makeObject(eq(DEFAULT_WORKSPACE_NAME));
        verify(poolableSessionFactory).activateObject(eq(DEFAULT_WORKSPACE_NAME), same(session));
        verify(poolableSessionFactory).passivateObject(eq(DEFAULT_WORKSPACE_NAME), same(pooledSession));
        verify(poolableSessionFactory).destroyObject(eq(DEFAULT_WORKSPACE_NAME), same(pooledSession));
        verifyNoMoreInteractions(poolableSessionFactory);
        verifyZeroInteractions(pooledSession);
    }
}
