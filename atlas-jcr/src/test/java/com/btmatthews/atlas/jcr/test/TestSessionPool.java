package com.btmatthews.atlas.jcr.test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import javax.jcr.Session;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.btmatthews.atlas.jcr.SessionPool;
import com.btmatthews.atlas.jcr.SessionPoolImpl;

public class TestSessionPool {

	private static final String DEFAULT_WORKSPACE_NAME = "default";

	@Mock
	private KeyedPoolableObjectFactory<String, Session> poolableSessionFactory;

	@Mock
	private Session pooledSession;

	private SessionPool sessionPool;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		sessionPool = new SessionPoolImpl(poolableSessionFactory);
	}

	@Test
	public void testBorrowSuccess() throws Exception {
		when(poolableSessionFactory.makeObject(DEFAULT_WORKSPACE_NAME))
				.thenReturn(pooledSession);
		final Session session = sessionPool.borrowSession(DEFAULT_WORKSPACE_NAME);
		assertSame(pooledSession, session);		
	}
}
