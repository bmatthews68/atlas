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

import javax.jcr.Session;

/**
 * This holder is used to manage the association of JCR sessions with threads
 * using a thread local variable.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 */
public class SessionHolder {

	/**
	 * Thread local holding the JCR session currently associated with the
	 * current thread.
	 */
	private static ThreadLocal<Session> SESSION = new ThreadLocal<Session>();

	/**
	 * Clear the thread local holding the JCR session associated with the
	 * current thread.
	 */
	public static void clearSession() {
		SESSION.set(null);
	}

	/**
	 * Get the JCR session associated with the current thread from the thread
	 * local.
	 * 
	 * @return The currently associated JCR session or {@code null} if no JCR
	 *         session is currently associated with the current thread.
	 */
	public static Session getSession() {
		return SESSION.get();
	}

	/**
	 * Associate a JCR session with the current thread by storing it in the
	 * thread local.
	 * 
	 * @param session
	 *            The JCR session that will be associated with the current
	 *            thread.
	 */
	public static void setSession(final Session session) {
		SESSION.set(session);
	}
}
