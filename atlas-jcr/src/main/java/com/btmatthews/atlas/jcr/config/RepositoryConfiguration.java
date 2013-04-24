/*
 * Copyright 2011-2012 Brian Matthews
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jcr.Repository;
import javax.jcr.Session;

@Configuration
public class RepositoryConfiguration {

    private RepositoryProvider repositoryProvider;
    private CredentialsProvider credentialsProvider;

    @Autowired
    public void setRepository(final RepositoryProvider repo) {
        repositoryProvider = repo;
    }

    @Autowired
    public void setCredentialsProvider(final CredentialsProvider creds) {
        credentialsProvider = creds;
    }

    @Bean
    @Autowired
    public KeyedPoolableObjectFactory<String, Session> poolableObjectFactory() {
        final Repository repository = repositoryProvider.getRepository();
        return new PoolableSessionFactory(repository, credentialsProvider);
    }

    @Bean(destroyMethod = "shutdown")
    @Autowired
    public SessionFactory sessionPool(final KeyedPoolableObjectFactory<String, Session> objectFactory) {
        return new PooledSessionFactory(objectFactory);
    }

    @Bean
    @Autowired
    public JCRAccessor jcrTemplate(final SessionFactory sessionFactory) {
        return new JCRTemplate(sessionFactory);
    }
}
