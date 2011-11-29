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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JCRTemplate implements JCRAccessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JCRTemplate.class);

	private SessionPool sessionPool;

	@Autowired
	public void setSessionPool(final SessionPool pool) {
		sessionPool = pool;
	}

	public void withRoot(final String workspaceName,
			final NodeVoidCallback callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final Node node = session.getRootNode();
				callback.doInSessionWithNode(session, node);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public <T> T withRoot(final String workspaceName,
			final NodeCallback<T> callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final Node node = session.getRootNode();
				return callback.doInSessionWithNode(session, node);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public void withNodePath(final String workspaceName, final String path,
			final NodeVoidCallback callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final Node node = session.getNode(path);
				callback.doInSessionWithNode(session, node);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public <T> T withNodePath(final String workspaceName, final String path,
			final NodeCallback<T> callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final Node node = session.getNode(path);
				return callback.doInSessionWithNode(session, node);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public void withNodeId(final String workspaceName, final String id,
			final NodeVoidCallback callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final Node node = session.getNodeByIdentifier(id);
				callback.doInSessionWithNode(session, node);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public <T> T withNodeId(final String workspaceName, final String id,
			final NodeCallback<T> callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final Node node = session.getNodeByIdentifier(id);
				return callback.doInSessionWithNode(session, node);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public void execute(final String workspaceName,
			final SessionVoidCallback callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				callback.doInSession(session);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public <T> T execute(final String workspaceName,
			final SessionCallback<T> callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				return callback.doInSession(session);
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}

	public <T> List<T> execute(final String workspaceName,
			final String statement, final String language,
			final NodeCallback<T> callback) {
		try {
			final Session session = sessionPool.borrowSession(workspaceName);
			try {
				final QueryManager queryManager = session.getWorkspace()
						.getQueryManager();
				final Query query = queryManager.createQuery(statement,
						language);
				final QueryResult queryResult = query.execute();
				final NodeIterator iterator = queryResult.getNodes();
				final List<T> results = new ArrayList<T>();
				while (iterator.hasNext()) {
					final Node node = iterator.nextNode();
					final T result = callback
							.doInSessionWithNode(session, node);
					results.add(result);
				}
				return results;
			} finally {
				sessionPool.returnSession(workspaceName, session);
			}
		} catch (final Exception e) {
			final String message = e.getMessage();
			LOGGER.debug(message, e);
			throw new RepositoryAccessException(message, e);
		}
	}
}
