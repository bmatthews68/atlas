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

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;

/**
 * A factory that creates poolable sessions used to connect to the Java Content
 * Repository.
 */
public class PoolableSessionFactory extends
		BaseKeyedPoolableObjectFactory<String, Session> {

	/**
	 * The Java Content Repository.
	 */
	private Repository repository;

	/**
	 * Provides the credentials used to login to the Java Content Repository.
	 */
	private CredentialsProvider credentialsProvider;

	/**
	 * Initialise the poolable object factory setting the repository and
	 * credentials providers
	 * 
	 * @param repo
	 *            Provides the Java Content Repository.
	 * @param creds
	 *            Provides the credentials.
	 */
	public PoolableSessionFactory(final Repository repo,
			final CredentialsProvider creds) {
		repository = repo;
		credentialsProvider = creds;
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
	public Session makeObject(final String key) throws Exception {
		final Credentials credentials = credentialsProvider
				.getGlobalCredentials();
		return repository.login(credentials, key);
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
	public boolean validateObject(final String key, final Session obj) {
		return obj.isLive();
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
	public void destroyObject(final String key, final Session obj)
			throws Exception {
		obj.logout();
	}

}
