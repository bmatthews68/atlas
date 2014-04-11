/*
 * Copyright 2011-2013 Brian Thomas Matthews
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
import com.btmatthews.atlas.jcr.impl.SimpleSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration when using a simple session factory.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
@Configuration
public class SimpleRepositoryConfiguration {

    /**
     * Used to get a reference to the repository.
     */
    private RepositoryProvider repositoryProvider;
    /**
     * Used to get references to the global and user-specific credentials.
     */
    private CredentialsProvider credentialsProvider;

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
     * Create the session factory bean.
     *
     * @return A {@link SimpleSessionFactory} object.
     */
    @Bean
    @Autowired
    public SessionFactory sessionFactory() {
        return new SimpleSessionFactory(repositoryProvider, credentialsProvider);
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
