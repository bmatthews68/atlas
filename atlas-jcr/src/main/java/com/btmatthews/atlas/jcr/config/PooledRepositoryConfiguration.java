/*
 * Copyright 2011-2013 Brian Matthews
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

package com.btmatthews.atlas.jcr.config;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import com.btmatthews.atlas.jcr.JCRAccessor;
import com.btmatthews.atlas.jcr.RepositoryProvider;
import com.btmatthews.atlas.jcr.SessionFactory;
import com.btmatthews.atlas.jcr.impl.JCRTemplate;
import com.btmatthews.atlas.jcr.impl.PoolableSessionFactory;
import com.btmatthews.atlas.jcr.impl.PooledSessionFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jcr.Repository;
import javax.jcr.Session;

/**
 * Configuration when using a session pool.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Configuration
public class PooledRepositoryConfiguration {

    /**
     * Used to get a reference to the repository.
     */
    private RepositoryProvider repositoryProvider;
    /**
     * Used to get references to the global and user-specific credentials.
     */
    private CredentialsProvider credentialsProvider;
    /**
     * Used to configure the behaviour of the session object pool.
     */
    private GenericKeyedObjectPool.Config poolConfiguration;

    /**
     * Used to inject the repository provider defined in another context.
     *
     * @param provider The repository provider.
     */
    @Autowired
    public void setRepository(final RepositoryProvider provider) {
        repositoryProvider = provider;
    }

    /**
     * Used to inject the credentials provider defined in another context.
     *
     * @param provider The credential provider.
     */
    @Autowired
    public void setCredentialsProvider(final CredentialsProvider provider) {
        credentialsProvider = provider;
    }

    /**
     * Used to inject the session object pool configuration.
     *
     * @param configuration The object pool configuration.
     */
    @Autowired(required = false)
    public void setPoolConfiguration(GenericKeyedObjectPool.Config configuration) {
        poolConfiguration = configuration;
    }

    /**
     * Create the keyed poolable object factory that will be used by the session object pool to create session objects.
     *
     * @return The keyed poolable object factory.
     */
    @Bean
    @Autowired
    public KeyedPoolableObjectFactory<String, Session> poolableObjectFactory() {
        final Repository repository = repositoryProvider.getRepository();
        return new PoolableSessionFactory(repository, credentialsProvider);
    }

    /**
     * Create the session factory that dispenses sessions from a keyed pool of session objects.
     *
     * @param objectFactory The poolable object factory that is used to create the session objects.
     * @return The session factory.
     */

    @Bean(destroyMethod = "shutdown")
    @Autowired
    public SessionFactory sessionFactory(final KeyedPoolableObjectFactory<String, Session> objectFactory) {
        if (poolConfiguration == null) {
            return new PooledSessionFactory(objectFactory);
        } else {
            return new PooledSessionFactory(objectFactory, poolConfiguration);
        }
    }

    /**
     * Create a {@link JCRAccessor} that is used to simplify interaction with the JCR repository.
     *
     * @param sessionFactory The session factory.
     * @return A {@link JCRTemplate} object.
     */
    @Bean
    @Autowired
    public JCRAccessor jcrTemplate(final SessionFactory sessionFactory) {
        return new JCRTemplate(sessionFactory, credentialsProvider);
    }
}
