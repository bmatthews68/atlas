package com.btmatthews.atlas.opencmis;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class RepositoryAccessException extends RuntimeException {

    public RepositoryAccessException() {
    }

    public RepositoryAccessException(final String message) {
        super(message);
    }

    public RepositoryAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RepositoryAccessException(final Throwable cause) {
        super(cause);
    }
}
