/*
 * Copyright 2011-2012 Brian Thomas Matthews
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

package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.SessionFactory;
import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import javax.jcr.Session;

/**
 * A pool that provides session objects used to interact with the Java Content
 * Repository. This implementation uses Commons Pool to provide the underlying
 * pool implementation.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class PooledSessionFactory implements SessionFactory {

    /**
     * The underlying object pool that holds the pooled sessions.
     */
    private KeyedObjectPool<String, Session> objectPool;

    /**
     * Used by the Spring Framework to inject the poolable object factory that
     * is used to create sessions.
     *
     * @param factory The poolable object factory.
     */
    public PooledSessionFactory(final KeyedPoolableObjectFactory<String, Session> factory) {
        objectPool = new GenericKeyedObjectPool<String, Session>(factory);
    }

    public PooledSessionFactory(final  KeyedPoolableObjectFactory<String, Session> factory, GenericKeyedObjectPool.Config config)  {
        objectPool = new GenericKeyedObjectPool<String, Session>(factory, config);
    }

    /**
     * Close the underlying object pool when the session pool is destroyed.
     *
     * @throws Exception If there was an error closing the underlying object pool.
     */
    public void shutdown() throws Exception {
        objectPool.close();
        objectPool = null;
    }

    /**
     * Borrow a session from the underlying object pool. The workspace name is
     * used as a key when obtaining a session from the underlying object pool.
     *
     * @param workspaceName The workspace name.
     * @return The session.
     * @throws Exception If there was an error obtaining the session from the
     *                   underlying object pool.
     * @see com.btmatthews.atlas.jcr.SessionFactory#getSession(String)
     */
    public Session getSession(final String workspaceName) throws Exception {
        return objectPool.borrowObject(workspaceName);
    }

    /**
     * Return a session to the underlying object pool. The workspace name is
     * used as a key when obtaining a session from the underlying object pool.
     *
     * @param workspaceName The workspace name.
     * @param session       The session.
     * @throws Exception If there was an error returning the session to the underlying
     *                   object pool.
     * @see com.btmatthews.atlas.jcr.SessionFactory#releaseSession(String, Session)
     */
    public void releaseSession(final String workspaceName, final Session session)
            throws Exception {
        objectPool.returnObject(workspaceName, session);
    }
}
