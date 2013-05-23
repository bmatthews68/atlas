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
 * A factory that provides session objects used to interact with the Java Content
 * Repository.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public interface SessionFactory {

    /**
     * Get a session for the named workspace.
     *
     * @param workspaceName The workspace name.
     * @return The session.
     * @throws Exception If there was an error obtaining a session.
     */
    Session getSession(String workspaceName) throws Exception;

    /**
     * Release a session for the named workspace.
     *
     * @param workspaceName The workspace name.
     * @param session       The session.
     * @throws Exception If there was an error releasing the session.
     */
    void releaseSession(String workspaceName, final Session session)
            throws Exception;
}
