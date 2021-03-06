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

package com.btmatthews.atlas.jcr;

import javax.jcr.Session;

/**
 * This callback is passed to {@link JCRAccessor} methods that perform perform operations on the JCR session and return
 * a typed result.
 *
 * @param <T> The result type.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public interface SessionCallback<T> {

    /**
     * The method that implements the callback.
     *
     * @param session The session.
     * @return The callback result.
     * @throws Exception The callback can throw an exception but it will get translated to a {@link RepositoryAccessException}.
     */
    T doInSession(Session session) throws Exception;
}
