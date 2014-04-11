package com.btmatthews.atlas.opencmis;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public interface OpenCMISAccessor {

    void execute(String workspaceName, SessionVoidCallback callback);

    <T> T execute(String workspaceName, SessionCallback<T> callback);

}
