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

package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import com.btmatthews.atlas.jcr.RepositoryProvider;
import com.btmatthews.atlas.jcr.SessionFactory;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;

/**
 * A simple session factory that does not preform any session pooling.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public final class SimpleSessionFactory implements SessionFactory {

    /**
     * The repository provider use used to get a reference to the repository.
     */
    private RepositoryProvider repositoryProvider;
    /**
     * The credentials provider is used to get the a reference to the global and user credentials.
     */
    private CredentialsProvider credentialsProvider;

    /**
     * Initialise the session factory setting the repository and credentials providers.
     *
     * @param repositoryProvider  The repository provider.
     * @param credentialsProvider The credentials provider.
     */
    public SimpleSessionFactory(final RepositoryProvider repositoryProvider, final CredentialsProvider credentialsProvider) {
        if (repositoryProvider == null) {
            throw new IllegalArgumentException("repositoryProvider cannot be null");
        }
        if (credentialsProvider == null) {
            throw new IllegalArgumentException("credentialsProvider cannot be null");
        }
        this.repositoryProvider = repositoryProvider;
        this.credentialsProvider = credentialsProvider;
    }

    /**
     * Get a session by logging into the repository using the global credentials.
     *
     * @param workspaceName The workspace name. If {@code null} then the default workspace is used.
     * @return The new session.
     * @throws Exception If there was a problem getting the session.
     */
    @Override
    public Session getSession(final String workspaceName) throws Exception {
        final Repository repository = repositoryProvider.getRepository();
        if (repository == null) {
            throw new IllegalStateException("The repository provider did not return the repository");
        }
        final Credentials credentials = credentialsProvider.getGlobalCredentials();
        if (credentials == null) {
            throw new IllegalStateException("The credentials provider did not return the global credentials");
        }
        if (workspaceName == null) {
            return repository.login(credentials);
        } else {
            return repository.login(credentials, workspaceName);
        }
    }

    /**
     * Release the session by logging out.
     *
     * @param workspaceName The workspace name.
     * @param session       The session.
     * @throws Exception If there was problem releasing the session.
     */
    @Override
    public void releaseSession(final String workspaceName, final Session session) throws Exception {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        }
        session.logout();
    }
}
