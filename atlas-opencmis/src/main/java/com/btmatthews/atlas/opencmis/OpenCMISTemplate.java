package com.btmatthews.atlas.opencmis;

import org.apache.chemistry.opencmis.client.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class OpenCMISTemplate implements OpenCMISAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCMISTemplate.class);

    private SessionPool sessionPool;

    public OpenCMISTemplate() {
    }

    public OpenCMISTemplate(final SessionPool pool) {
        sessionPool = pool;
    }

    public void setSessionPool(final SessionPool pool) {
        sessionPool = pool;
    }

    public void execute(String workspaceName, SessionVoidCallback callback) {
        try {
            Session session = sessionPool.borrowSession(workspaceName);
            try {
                callback.doInSession(session);
            } finally {
                sessionPool.returnSession(workspaceName, session);
            }
        } catch (final Exception e) {
            final String message = e.getMessage();
            LOGGER.debug(message, e);
            throw new RepositoryAccessException(message, e);
        }
    }

    public <T> T execute(String workspaceName, SessionCallback<T> callback) {
        try {
            final Session session = sessionPool.borrowSession(workspaceName);
            try {
                return callback.doInSession(session);
            } finally {
                sessionPool.returnSession(workspaceName, session);
            }
        } catch (final Exception e) {
            final String message = e.getMessage();
            LOGGER.debug(message, e);
            throw new RepositoryAccessException(message, e);
        }
    }

}
