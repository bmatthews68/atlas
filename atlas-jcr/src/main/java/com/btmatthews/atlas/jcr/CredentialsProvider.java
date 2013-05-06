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

import javax.jcr.Credentials;

/**
 * Implementations of {@link CredentialsProvider} are used to obtain the global application wide credentials that are
 * required to log on to the repository when there is no end-user or an administrative operations is being performed.
 * If there is an end-user then the global credentials are used to create the initial session then the the end-user
 * credentials are used to spawn a session with the end-user's credentials.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface CredentialsProvider {

    /**
     * Get global application wide {@link Credentials} used to log in to the repository.
     *
     * @return The application wide credentials.
     */
    Credentials getGlobalCredentials();

    /**
     * Get the {@link Credentials} used to log into the repository as the current end-user.
     *
     * @return The end-user credentials.
     */
    Credentials getUserCredentials();
}
