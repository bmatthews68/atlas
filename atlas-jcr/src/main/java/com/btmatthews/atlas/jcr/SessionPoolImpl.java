/*
 * Copyright 2011 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.atlas.jcr;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Session;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A pool that provides session objects used to interact with the Java Content
 * Repository. This implementation uses Commons Pool to provide the underlying
 * pool implementation.
 */
@Component
public class SessionPoolImpl implements SessionPool {

	/**
	 * The underlying object pool that holds the pooled sessions.
	 */
	private KeyedObjectPool objectPool;

	/**
	 * The poolable object factory that is used to create sessions.
	 */
	private KeyedPoolableObjectFactory poolableSessionFactory;

	/**
	 * Used by the Spring Framework to inject the poolable object factory that
	 * is used to create sessions.
	 * 
	 * @param poolableObjectFactory
	 *            The poolable object factory.
	 */
	@Autowired
	public void setPoolableObjectFactory(
			final KeyedPoolableObjectFactory poolableObjectFactory) {
		poolableSessionFactory = poolableObjectFactory;
	}

	/**
	 * Initialise the underlying object pool after the session pool has been
	 * instantiated and auto-wired.
	 */
	@PostConstruct
	public void init() {
		objectPool = new GenericKeyedObjectPool(poolableSessionFactory);
	}

	/**
	 * Close the underlying object pool when the session pool is destroyed.
	 * 
	 * @throws Exception
	 *             If there was an error closing the underlying object pool.
	 */
	@PreDestroy
	public void shutdown() throws Exception {
		objectPool.close();
		objectPool = null;
	}

	/**
	 * Borrow a session from the underlying object pool. The workspace name is
	 * used as a key when obtaining a session from the underlying object pool.
	 * 
	 * @param workspaceName
	 *            The workspace name.
	 * @return The session.
	 * @throws Exception
	 *             If there was an error obtaining the session from the
	 *             underlying object pool.
	 * @see SessionPool#borrowSession(String)
	 */
	public Session borrowSession(final String workspaceName) throws Exception {
		return (Session) objectPool.borrowObject(workspaceName);
	}

	/**
	 * Return a session to the underlying object pool. The workspace name is
	 * used as a key when obtaining a session from the underlying object pool.
	 * 
	 * @param workspaceName
	 *            The workspace name.
	 * @param session
	 *            The session.
	 * @throws Exception
	 *             If there was an error returning the session to the underlying
	 *             object pool.
	 * @see SessionPool#returnSession(String, Session)
	 */
	public void returnSession(final String workspaceName, final Session session)
			throws Exception {
		objectPool.returnObject(workspaceName, session);
	}
}
