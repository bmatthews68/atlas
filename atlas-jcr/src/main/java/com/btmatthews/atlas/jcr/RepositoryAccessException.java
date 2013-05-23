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

package com.btmatthews.atlas.jcr;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class RepositoryAccessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -3931707994177411056L;

    /**
     * Initialise the {@link RepositoryAccessException} with an error message.
     *
     * @param msg The error message.
     */
    public RepositoryAccessException(final String msg) {
        super(msg);
    }

    /**
     * Initialise the {@link RepositoryAccessException} with an error message and a root cause.
     *
     * @param msg   The error message.
     * @param cause The root cause.
     */
    public RepositoryAccessException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
