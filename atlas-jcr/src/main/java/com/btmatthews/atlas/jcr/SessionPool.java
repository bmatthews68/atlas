/*
 * Copyright 2011 Brian Matthews
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
 * A pool that provides session objects used to interact with the Java Content
 * Repository.
 */
public interface SessionPool {

	/**
	 * Borrow a session for the named workspace from the session pool.
	 * 
	 * @param workspaceName
	 *            The workspace name.
	 * @return The session.
	 * @throws Exception
	 *             If there was an error obtaining a session from the session
	 *             pool.
	 */
	Session borrowSession(String workspaceName) throws Exception;

	/**
	 * Return a session for the named workspace to the session pool.
	 * 
	 * @param workspaceName
	 *            The workspace name.
	 * @param session
	 *            The session.
	 * @throws Exception
	 *             If there was an error returning a session to the session
	 *             pool.
	 */
	void returnSession(String workspaceName, final Session session)
			throws Exception;
}
