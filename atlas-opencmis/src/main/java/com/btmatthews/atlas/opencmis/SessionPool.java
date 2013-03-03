package com.btmatthews.atlas.opencmis;

import org.apache.chemistry.opencmis.client.api.Session;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface SessionPool {

    Session borrowSession(String workspaceName) throws Exception;

    void returnSession(String workspaceName, Session session)
            throws Exception;
}
