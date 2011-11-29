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

import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A factory that creates poolable sessions used to connect to the Java Content
 * Repository.
 */
@Component
public class PoolableSessionFactory extends BaseKeyedPoolableObjectFactory {

	/**
	 * The Java Content Repository.
	 */
	private Repository repository;

	/**
	 * Used by the Spring Framework to inject the Java Content Repository.
	 * 
	 * @param repo
	 *            The Java Content Repository.
	 */
	@Autowired
	public void setRepository(final Repository repo) {
		repository = repo;
	}

	/**
	 * Create a new session object by logging in to the Java Content Repository
	 * anonymously.
	 * 
	 * @param key
	 *            The workspace name.
	 * @return The session.
	 * @throws Exception
	 *             If there was an error logging into from the session.
	 */
	@Override
	public Object makeObject(final Object key) throws Exception {
		return repository.login((String) key);
	}

	/**
	 * Validate a session by checking that it is still live.
	 * 
	 * @param key
	 *            The workspace name.
	 * @param obj
	 *            The session.
	 * @throws Exception
	 *             If there was an error validating the session.
	 */
	@Override
	public boolean validateObject(final Object key, final Object obj) {
		return ((Session) obj).isLive();
	}

	/**
	 * Destroy the pooled session by logging out from the Java Content
	 * Repository.
	 * 
	 * @param key
	 *            The workspace name.
	 * @param obj
	 *            The session.
	 * @throws Exception
	 *             If there was an error logging out from the session.
	 */
	@Override
	public void destroyObject(final Object key, final Object obj)
			throws Exception {
		((Session) obj).logout();
	}

}
