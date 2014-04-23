/*
 * Copyright 2011-2012 Brian Thomas Matthews
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
import org.springframework.util.StringUtils;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class JCRTemplate implements JCRAccessor {
    private final static String BORROW_SESSION_RETURNED_NULL = "SessionFactory#getSession(String) returned null";
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
    private SessionFactory sessionFactory;
    private CredentialsProvider credentialsProvider;

    /**
     * The default constructor.
     */
    public JCRTemplate() {
    }

    /**
     * Initialise the JCR template with a session factory.
     *
     * @param factory The session factory.
     */
    public JCRTemplate(final SessionFactory factory) {
        sessionFactory = factory;
    }

    /**
     * Initialise the JCR template with a session factory and a credentials provider.
     *
     * @param factory  The session factory.
     * @param provider The credentials provider.
     */
    public JCRTemplate(final SessionFactory factory, final CredentialsProvider provider) {
        sessionFactory = factory;
        credentialsProvider = provider;
    }

    /**
     * Used to inject the session pool.
     *
     * @param pool The session pool.
     */
    public void setSessionFactory(final SessionFactory pool) {
        sessionFactory = pool;
    }

    /**
     * Used to inject the credentials provider.
     *
     * @param provider The credentials provider.
     */
    public void setCredentialsProvider(final CredentialsProvider provider) {
        credentialsProvider = provider;
    }

    /**
     * Determine if the node contains the named property.
     *
     * @param node The node.
     * @param name The property name.
     * @return {@code true} if the node contains the named property. {@code false} otherwise.
     */
    public boolean hasProperty(final Node node, final String name) {
        try {
            return node.hasProperty(name);
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    /**
     * Determine if the node contains all the named properties.
     *
     * @param node  The node.
     * @param names The property names.
     * @return {@code true} if the node contains the named property. {@code false} otherwise.
     */
    public boolean hasProperties(final Node node, final String... names) {
        for (final String name : names) {
            if (!hasProperty(node, name)) {
                return false;
            }
        }
        return true;
    }

    public Binary getBinaryProperty(final Node node, final String name) {
        return getBinaryProperty(node, name, null);
    }

    public Binary getBinaryProperty(final Node node, final String name, final Binary defaultValue) {
        try {
            return node.getProperty(name).getBinary();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public BigDecimal getBigDecimalProperty(final Node node, final String name) {
        return getBigDecimalProperty(node, name, null);
    }

    public BigDecimal getBigDecimalProperty(final Node node, final String name, final BigDecimal defaultValue) {
        try {
            return node.getProperty(name).getDecimal();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Boolean getBooleanProperty(final Node node, final String name) {
        return getBooleanProperty(node, name, null);
    }

    public Boolean getBooleanProperty(final Node node, final String name, final Boolean defaultValue) {
        try {
            return node.getProperty(name).getBoolean();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Calendar getCalendarProperty(final Node node, final String name) {
        return getCalendarProperty(node, name, null);
    }

    public Calendar getCalendarProperty(final Node node, final String name, final Calendar defaultValue) {
        try {
            return node.getProperty(name).getDate();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Double getDoubleProperty(final Node node, final String name) {
        return getDoubleProperty(node, name, null);
    }

    public Double getDoubleProperty(final Node node, final String name, final Double defaultValue) {
        try {
            return node.getProperty(name).getDouble();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Long getLongProperty(final Node node, final String name) {
        return getLongProperty(node, name, null);
    }

    public Long getLongProperty(final Node node, final String name, final Long defaultValue) {
        try {
            return node.getProperty(name).getLong();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public String getPathProperty(final Node node, final String name) {
        return getPathProperty(node, name, null);
    }

    public String getPathProperty(final Node node, final String name, final String defaultValue) {
        try {
            return node.getProperty(name).getString();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Node getReferenceProperty(final Node node, final String name) {
        return getReferenceProperty(node, name, null);
    }

    public Node getReferenceProperty(final Node node, final String name, final Node defaultValue) {
        try {
            return node.getProperty(name).getNode();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public String getStringProperty(final Node node, final String name) {
        return getStringProperty(node, name, null);
    }

    public String getStringProperty(final Node node, final String name, final String defaultValue) {
        try {
            return node.getProperty(name).getString();
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public URI getURIProperty(final Node node, final String name) {
        return getURIProperty(node, name, null);
    }

    public URI getURIProperty(final Node node, final String name, final URI defaultValue) {
        try {
            return new URI(node.getProperty(name).getString());
        } catch (final PathNotFoundException e) {
            return defaultValue;
        } catch (final RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } catch (final URISyntaxException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Node getOrCreateNode(final Node node,
                                final String leafType,
                                final String name) {
        try {
            if (node.hasNode(name)) {
                return node.getNode(name);
            } else {
                return node.addNode(name, leafType);
            }
        } catch (RepositoryException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public Node getOrCreateNode(final Node node,
                                final String intermediateType,
                                final String leafType,
                                final String path) {
        return getOrCreateNode(node, intermediateType, leafType, StringUtils.split(path, "/"));

    }

    public Node getOrCreateNode(final Node node,
                                final String intermediateType,
                                final String leafType,
                                final String... names) {
        Node parent = node;
        int i = 0;
        while (i < names.length - 1) {
            parent = getOrCreateNode(parent, intermediateType, names[i++]);
        }
        return getOrCreateNode(parent, leafType, names[i]);

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
        return withSession(workspaceName, session -> {
            final Node node = session.getRootNode();
            if (node == null) {
                throw new RepositoryAccessException(GET_ROOT_NODE_RETURNED_NULL);
            }
            return callback.doInSessionWithNode(session, node);
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

    public <T> T withNodePath(final String workspaceName,
                              final String path,
                              final NodeCallback<T> callback) {
        return withNodePath(
                workspaceName,
                path,
                callback,
                JCRTemplate::defaultNotFoundCallback,
                JCRTemplate::defaultErrorCallback);
    }

    public <T> T withNodePath(final String workspaceName,
                              final String path,
                              final NodeCallback<T> found,
                              final ErrorCallback<T> notFound) {
        return withNodePath(
                workspaceName,
                path,
                found,
                notFound,
                JCRTemplate::defaultErrorCallback);
    }

    public <T> T withNodePath(final String workspaceName,
                              final String path,
                              final NodeCallback<T> found,
                              final ErrorCallback<T> notFound,
                              final ErrorCallback<T> error) {
        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert path != null && Pattern.matches(PATH_PATTERN, path);
        assert found != null;
        assert notFound != null;
        assert error != null;

        return withSession(
                workspaceName,
                session -> {
                    try {
                        final Node node = session.getNode(path);
                        return found.doInSessionWithNode(session, node);
                    } catch (PathNotFoundException e) {
                        return notFound.doInSessionWithException(session, e);
                    } catch (final RepositoryException e) {
                        return error.doInSessionWithException(session, e);
                    }
                }
        );
    }

    /**
     * Perform a callback operation on the node identified by {@code id} in the workspace named {@code workspaceName}
     * and return the result of the callback operation to the caller.
     *
     * @param workspaceName The name of the workspace.
     * @param id            The identifier of the node.
     * @param found         The callback.
     * @param <T>           The type returned by the callback operation.
     * @return The result of the callback operation.
     */
    public <T> T withNodeId(final String workspaceName,
                            final String id,
                            final NodeCallback<T> found) {
        return withNodeId(
                workspaceName,
                id,
                found,
                JCRTemplate::defaultNotFoundCallback,
                JCRTemplate::defaultErrorCallback);
    }

    public <T> T withNodeId(final String workspaceName,
                            final String id,
                            final NodeCallback<T> found,
                            final ErrorCallback<T> notFound) {
        return withNodeId(workspaceName,
                id,
                found,
                notFound,
                JCRTemplate::defaultErrorCallback);
    }

    public <T> T withNodeId(final String workspaceName,
                            final String id,
                            final NodeCallback<T> found,
                            final ErrorCallback<T> notFound,
                            final ErrorCallback<T> error) {
        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert id != null && Pattern.matches(ID_PATTERN, id);
        assert found != null;
        assert notFound != null;
        assert error != null;

        return withSession(workspaceName, session -> {
            try {
                final Node node = session.getNodeByIdentifier(id);
                return found.doInSessionWithNode(session, node);
            } catch (final PathNotFoundException e) {
                return notFound.doInSessionWithException(session, e);
            } catch (final RepositoryException e) {
                return error.doInSessionWithException(session, e);
            }
        });
    }

    private static <T> T defaultNotFoundCallback(final Session session,
                                                 final Exception e)
            throws Exception {
        throw new RepositoryAccessException(GET_NODE_RETURNED_NULL);
    }

    private static <T> T defaultErrorCallback(final Session session,
                                              final Exception e)
            throws Exception {
        throw e;
    }

    /**
     * @param workspaceName The name of the workspace.
     * @param callback      The callback that implements the function to be executed.
     * @param <T>           The type returned by the function.
     * @return The result of the function.
     */
    public <T> T withSession(final String workspaceName,
                             final SessionCallback<T> callback) {

        assert workspaceName != null && Pattern.matches(WORKSPACE_PATTERN, workspaceName);
        assert callback != null;

        try {
            final Session session = sessionFactory.getSession(workspaceName);
            if (session == null) {
                throw new RepositoryAccessException(BORROW_SESSION_RETURNED_NULL);
            }
            try {
                if (credentialsProvider == null) {
                    return callback.doInSession(session);
                } else {
                    final Credentials credentials = credentialsProvider.getUserCredentials();
                    if (credentials == null) {
                        return callback.doInSession(session);
                    } else {
                        final Session impersonatedSession = session.impersonate(credentials);
                        try {
                            return callback.doInSession(impersonatedSession);
                        } finally {
                            impersonatedSession.logout();
                        }
                    }
                }
            } finally {
                sessionFactory.releaseSession(workspaceName, session);
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
     * @param workspaceName The name of the workspace.
     * @param statement     The query statement.
     * @param language      The query language.
     * @param callback      The callback operation to be performed on each item in the query result.
     * @param offset        The offset in the query result to start processing.
     * @param limit         The maximum number of items to process.
     * @param <T>           The type returned by the callback operation.
     * @return A {@link List} containing the callback results.
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

        return withSession(workspaceName,
                session -> {
                    final NodeIterator iterator = getQueryResults(session, statement, language, EMPTY_PARAMETERS, offset, limit);
                    final List<T> results = new ArrayList<>((int) iterator.getSize());
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
        );
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
