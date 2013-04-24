package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import com.btmatthews.atlas.jcr.RepositoryProvider;
import com.btmatthews.atlas.jcr.SessionFactory;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public final class SimpleSessionFactory implements SessionFactory {

    private RepositoryProvider repositoryProvider;
    private CredentialsProvider credentialsProvider;

    public SimpleSessionFactory(final RepositoryProvider repositoryProvider, final CredentialsProvider credentialsProvider) {
        this.repositoryProvider = repositoryProvider;
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public Session getSession(final String workspaceName) throws Exception {
        final Repository repository = repositoryProvider.getRepository();
        final Credentials credentials = credentialsProvider.getGlobalCredentials();
        return repository.login(credentials, workspaceName);
    }

    @Override
    public void releaseSession(final String workspaceName, final Session session) throws Exception {
        session.logout();
    }
}
