/*
 * Copyright 2011-2012 Brian Matthews
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

package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

public class JCRTemplate implements JCRAccessor {
    private final static String BORROW_SESSION_RETURNED_NULL = "SessionPool#borrowSession(String) returned null";
    private final static String GET_ROOT_NODE_RETURNED_NULL = "Session#getRootNode() returned null.";
    private final static String GET_NODE_RETURNED_NULL = "Session#getNode(String) returned null";
    private final static String GET_NODE_BY_IDENTIFIER_RETURNED_NULL = "Session#getNodeByIdentifier(String) returned null";
    private final static String GET_WORKSPACE_RETURNED_NULL = "Session#getWorkspace() returned null";
    private final static String GET_QUERY_MANAGER_RETURNED_NULL = "Workspace#getQueryManager() return null";
    private final static String CREATE_QUERY_RETURNED_NULL = "QueryManager#createQuery(String, String) returned null";
    private final static String EXECUTE_RETURNED_NULL = "Query#execute() returned null";
    private final static String GET_NODES_RETURNED_NULL = "QueryResutlt#getNodes() returned null";
    private final static String NEXT_NODE_RETURNED_NULL = "NodeIterator#nextNode() returned null";
    private final static String GET_VALUE_FACTORY_RETURNED_NULL = "Session#getValueFactory() returned null";
    private final static String UNSUPPORTED_PARAMETER_TYPE = "Parameter type not supported. name={0}, class={1}";
    private final static String WORKSPACE_PATTERN = ".+";
    private final static String ID_PATTERN = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private final static String PATH_PATTERN = ".+";
    private final static String QUERY_PATTERN = ".+";
    private final static String LANGUAGE_PATTERN = "(JCR-SQL2)|(JCR-JQOM)";
    private static final Logger LOGGER = LoggerFactory.getLogger(JCRTemplate.class);
    private static final Map<String, Object> EMPTY_PARAMETERS = new HashMap<String, Object>();
    private SessionPool sessionPool;

    /**
     * The default constructor.
     */
    public JCRTemplate() {
    }

    /**
     * Initialise the JCR template with a session pool.
     *
     * @param pool The session pool.
     */
    public JCRTemplate(final SessionPool pool) {
        sessionPool = pool;
    }

    /**
     * Used to inject the session pool.
     *
     * @param pool The session pool.
     */
    public void setSessionPool(final SessionPool pool) {
        sessionPool = pool;
    }

    /**
     * Perform an operation against the root node of the named workspace.
     *
     * @param workspaceName The workspace name.
     * @param callback      The callback that implements the operation to be performed.
     */
    public void withRoot(final String workspaceName,
                         final NodeVoidCallback callback) {
        withSession(workspaceName, new SessionVoidCallback() {
            public void doInSession(final Session session) throws RepositoryException {
                final Node node = session.getRootNode();
                if (node == null) {
                    throw new RepositoryAccessException(GET_ROOT_NODE_RETURNED_NULL);
                }
                callback.doInSessionWithNode(session, node);
            }
        });
    }

    /**
     * Execute a function on the root node of the named workspace.
     *
     * @param workspaceName The workspace name.
     * @param callback      The callback that implements the function to be executed.
     * @param <T>           The type returned by the function.
     * @return The result returned by the function.
     */

    public <T> T withRoot(final String workspaceName,
                          final NodeCallback<T> callback) {
        return withSession(workspaceName, new SessionCallback<T>() {
            public T doInSession(final Session session) throws RepositoryException {
                final Node node = session.getRootNode();
                if (node == null) {
                    throw new RepositoryAccessException(GET_ROOT_NODE_RETURNED_NULL);
                }
                return callback.doInSessionWithNode(session, node);
            }
        });
    }

    /**
     * Perform an operation against a node at the specified path in the named workspace.
     *
     * @param workspaceName The workspace name.
     * @param path          The path to the node.
     * @param callback      The callback that implements the operation to be performed.
     */
    public void withNodePath(final String workspaceName, final String path,
                             final NodeVoidCallback callback) {
        withSession(workspaceName, new SessionVoidCallback() {
            public void doInSession(final Session session) throws RepositoryException {
                final Node node = session.getNode(path);
                if (node == null) {
                    throw new RepositoryAccessException(GET_NODE_RETURNED_NULL);
                }
                callback.doInSessionWithNode(session, node);
            }
        });
    }

    /**
     * Execute a function on a node at the specified path in the named workspace.
     *
     * @param workspaceName The named workspace.
     * @param path          The path to the node.
     * @param callback      The callback that implements the function to be executed.
     * @param <T>           The type returned by the function.
     * @return The result of the function.
     */

    public <T> T withNodePath(final String workspaceName, final String path,
                              final NodeCallback<T> callback) {
        return withSession(workspaceName, new SessionCallback<T>() {
            public T doInSession(final Session session) throws RepositoryException {
                final Node node = session.getNode(path);
                if (node == null) {
                    throw new RepositoryAccessException(GET_NODE_RETURNED_NULL);
                }
                return callback.doInSessionWithNode(session, node);
            }
        });
    }

    /**
     * @param workspaceName
     * @param id
     * @param callback
     */
    public void withNodeId(final String workspaceName, final String id,
                           final NodeVoidCallback callback) {
        withSession(workspaceName, new SessionVoidCallback() {
            public void doInSession(final Session session) throws RepositoryException {
                final Node node = session.getNodeByIdentifier(id);
                if (node == null) {
                    throw new RepositoryAccessException(GET_NODE_BY_IDENTIFIER_RETURNED_NULL);
                }
                callback.doInSessionWithNode(session, node);
            }
        });
    }

    /**
     * @param workspaceName
     * @param id
     * @param callback      The callback that implements the function to be executed.
     * @param <T>           The type returned by the function.
     * @return The result of the function.
     */
    public <T> T withNodeId(final String workspaceName,
                            final String id,
                            final NodeCallback<T> callback) {

        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert id != null && Pattern.matches(ID_PATTERN, id);
        assert callback != null;

        return withSession(workspaceName, new SessionCallback<T>() {
            public T doInSession(final Session session) throws RepositoryException {
                final Node node = session.getNodeByIdentifier(id);
                if (node == null) {
                    throw new RepositoryAccessException(GET_NODE_BY_IDENTIFIER_RETURNED_NULL);
                }
                return callback.doInSessionWithNode(session, node);
            }
        });
    }

    /**
     * @param workspaceName
     * @param callback
     */
    public void withSession(final String workspaceName,
                            final SessionVoidCallback callback) {

        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert callback != null;

        try {
            final Session session = sessionPool.borrowSession(workspaceName);
            if (session == null) {
                throw new RepositoryAccessException(BORROW_SESSION_RETURNED_NULL);
            }
            try {
                callback.doInSession(session);
            } finally {
                sessionPool.returnSession(workspaceName, session);
            }
        } catch (final RepositoryAccessException e) {
            LOGGER.debug(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            final String message = e.getMessage();
            LOGGER.debug(message, e);
            throw new RepositoryAccessException(message, e);
        }
    }

    /**
     * @param workspaceName
     * @param callback      The callback that implements the function to be executed.
     * @param <T>           The type returned by the function.
     * @return The result of the function.
     */
    public <T> T withSession(final String workspaceName,
                             final SessionCallback<T> callback) {

        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert callback != null;

        try {
            final Session session = sessionPool.borrowSession(workspaceName);
            if (session == null) {
                throw new RepositoryAccessException(BORROW_SESSION_RETURNED_NULL);
            }
            try {
                return callback.doInSession(session);
            } finally {
                sessionPool.returnSession(workspaceName, session);
            }
        } catch (final RepositoryAccessException e) {
            LOGGER.debug(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            final String message = e.getMessage();
            LOGGER.debug(message, e);
            throw new RepositoryAccessException(message, e);
        }
    }

    /**
     * @param workspaceName The workspace name.
     * @param statement     The query statement.
     * @param language      The query language.
     * @param callback      The callback to be invoked for each node in the return set.
     * @throws RepositoryAccessException If there was an error executing the query or processing the results.
     */
    public void withQueryResults(final String workspaceName,
                                 final String statement,
                                 final String language,
                                 final NodeVoidCallback callback,
                                 final long offset,
                                 final long limit) {

        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert statement != null && Pattern.matches(QUERY_PATTERN, statement);
        assert language != null && Pattern.matches(LANGUAGE_PATTERN, language);
        assert callback != null;
        assert offset >= 0;
        assert limit > 0;

        withSession(workspaceName, new SessionVoidCallback() {
            public void doInSession(final Session session) throws RepositoryException {
                final NodeIterator iterator = getQueryResults(session, statement, language, EMPTY_PARAMETERS, offset, limit);
                while (iterator.hasNext()) {
                    final Node node = iterator.nextNode();
                    if (node == null) {
                        throw new RepositoryAccessException(NEXT_NODE_RETURNED_NULL);
                    }
                    callback.doInSessionWithNode(session, node);
                }
            }
        });
    }

    /**
     * @param workspaceName
     * @param statement
     * @param language
     * @param callback
     * @param offset
     * @param limit
     * @param <T>
     * @return
     * @throws RepositoryAccessException If there was an error executing the query or processing the results.
     */
    public <T> List<T> withQueryResults(final String workspaceName,
                                        final String statement,
                                        final String language,
                                        final NodeCallback<T> callback,
                                        final long offset,
                                        final long limit) {

        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert statement != null && Pattern.matches(QUERY_PATTERN, statement);
        assert language != null && Pattern.matches(LANGUAGE_PATTERN, language);
        assert callback != null;
        assert offset >= 0;
        assert limit > 0;

        return withSession(workspaceName, new SessionCallback<List<T>>() {
            public List<T> doInSession(final Session session) throws RepositoryException {
                final NodeIterator iterator = getQueryResults(session, statement, language, EMPTY_PARAMETERS, offset, limit);
                final List<T> results = new ArrayList<T>((int) iterator.getSize());
                while (iterator.hasNext()) {
                    final Node node = iterator.nextNode();
                    if (node == null) {
                        throw new RepositoryAccessException(NEXT_NODE_RETURNED_NULL);
                    }
                    final T result = callback.doInSessionWithNode(session, node);
                    results.add(result);
                }
                return results;
            }
        });
    }

    /**
     * Create and execute a query returning an iterator that can be used to iterate through a subset of the query results.
     *
     * @param session    The JCR session.
     * @param statement  The query statement.
     * @param language   The query language.
     * @param parameters Query parameters.
     * @param offset     The starting offset in the result set.
     * @param limit      The maximum number of records to return from the result set.
     * @return A {@link NodeIterator} that can be used to iterate through the subset of the query results.
     * @throws RepositoryException       Thrown by the JCR API operations if there is an error.
     * @throws RepositoryAccessException If there was an unexpected result from a JCR API operation.
     */

    private NodeIterator getQueryResults(final Session session,
                                         final String statement,
                                         final String language,
                                         final Map<String, Object> parameters,
                                         final long offset,
                                         final long limit) throws RepositoryException {
        assert session != null;
        assert statement != null && Pattern.matches(QUERY_PATTERN, statement);
        assert language != null && Pattern.matches(LANGUAGE_PATTERN, language);
        assert offset >= 0;
        assert limit > 0;

        final Workspace workspace = session.getWorkspace();
        if (workspace == null) {
            throw new RepositoryAccessException(GET_WORKSPACE_RETURNED_NULL);
        }
        final QueryManager queryManager = workspace.getQueryManager();
        if (queryManager == null) {
            throw new RepositoryAccessException(GET_QUERY_MANAGER_RETURNED_NULL);
        }
        final Query query = queryManager.createQuery(statement, language);
        if (query == null) {
            throw new RepositoryAccessException(CREATE_QUERY_RETURNED_NULL);
        }
        query.setOffset(offset);
        query.setLimit(limit);
        if (!parameters.isEmpty()) {
            final ValueFactory valueFactory = session.getValueFactory();
            if (valueFactory == null) {
                throw new RepositoryAccessException(GET_VALUE_FACTORY_RETURNED_NULL);
            }
            for (final Map.Entry<String, Object> parameter : parameters.entrySet()) {
                bindValue(query, valueFactory, parameter.getKey(), parameter.getValue());
            }
        }
        final QueryResult queryResult = query.execute();
        if (queryResult == null) {
            throw new RepositoryAccessException(EXECUTE_RETURNED_NULL);
        }
        final NodeIterator iterator = queryResult.getNodes();
        if (iterator == null) {
            throw new RepositoryAccessException(GET_NODES_RETURNED_NULL);
        }
        return iterator;
    }

    private void bindValue(final Query query,
                           final ValueFactory valueFactory,
                           final String name,
                           final Object value)
            throws RepositoryException {
        if (value instanceof String) {
            query.bindValue(name, valueFactory.createValue((String) value));
        } else {
            throw new RepositoryAccessException(MessageFormat.format(UNSUPPORTED_PARAMETER_TYPE, name, value.getClass().getSimpleName()));
        }
    }

}