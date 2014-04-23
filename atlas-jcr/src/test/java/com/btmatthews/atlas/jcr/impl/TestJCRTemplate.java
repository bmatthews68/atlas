package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestJCRTemplate {

    private static final String TEST_WORKSPACE_NAME = "test";
    private static final String TEST_STATEMENT = "select * from [nt:base]";
    private static final String TEST_ID = "00000000-0000-0000-0000-000000000000";
    private static final String TEST_PATH = "/home/admin/data";
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Property nameProperty;
    @Mock
    private Node node;
    @Mock
    private Node rootNode;
    private JCRAccessor jcrTemplate;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        final JCRTemplate jcrTemplateImpl = new JCRTemplate();
        jcrTemplateImpl.setSessionFactory(sessionFactory);
        jcrTemplate = jcrTemplateImpl;
        when(sessionFactory.getSession(anyString())).thenReturn(session);
        when(session.getRootNode()).thenReturn(rootNode);
        when(nameProperty.getString()).thenReturn("style.css");
    }

    @Test
    public void initialiseSessionPoolUsingConstructor() throws Exception {
        final SessionCallback<Object> callback = mock(SessionCallback.class);
        new JCRTemplate(sessionFactory).withSession(TEST_WORKSPACE_NAME, callback);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(callback).doInSession(same(session));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, callback);
        verifyZeroInteractions(session);
    }

    @Test
    public void hasPropertyExisting() throws Exception {
        when(node.hasProperty(eq(Property.JCR_NAME))).thenReturn(true);
        assertTrue(jcrTemplate.hasProperty(node, Property.JCR_NAME));
    }

    @Test
    public void hasPropertyNonExisting() throws Exception {
        when(node.hasProperty(eq(Property.JCR_NAME))).thenReturn(false);
        assertFalse(jcrTemplate.hasProperty(node, Property.JCR_NAME));
    }

    @Test
    public void getStringPropertyExistingWithoutDefault() throws Exception {
        when(node.getProperty(eq(Property.JCR_NAME))).thenReturn(nameProperty);
        assertEquals("style.css", jcrTemplate.getStringProperty(node, Property.JCR_NAME));
    }

    @Test
    public void getStringPropertyNonExistingWithoutDefault() throws Exception {
        when(node.getProperty(eq(Property.JCR_NAME))).thenThrow(PathNotFoundException.class);
        assertNull(jcrTemplate.getStringProperty(node, Property.JCR_NAME));
    }

    @Test
    public void getStringPropertyNonExistingWithDefault() throws Exception {
        when(node.getProperty(eq(Property.JCR_NAME))).thenThrow(PathNotFoundException.class);
        assertEquals("style.css", jcrTemplate.getStringProperty(node, Property.JCR_NAME, "style.css"));
    }

    @Test
    public void withRootUsingVoidCallbackShouldSucceed() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getRootNode();
        verify(callback).doInSessionWithNode(same(session), same(rootNode));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
    }

    @Test
    public void withRootNodeUsingVoidCallbackShouldFailWhenBorrowSessionReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(sessionFactory.getSession(eq(TEST_WORKSPACE_NAME))).thenReturn(null);
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingVoidCallbackShouldFailWhenGetRootNodeThrowsException() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(session.getRootNode()).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getRootNode();
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingVoidCallbackShouldFailWhenGetRootNodeReturnsNull() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(session.getRootNode()).thenReturn(null);
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getRootNode();
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingVoidCallbackShouldFailWhenCallbackThrowsException() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        doThrow(RepositoryException.class).when(callback).doInSessionWithNode(same(session), same(rootNode));
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getRootNode();
            verify(callback).doInSessionWithNode(same(session), same(rootNode));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingTypeCallbackShouldSucceed() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Object result = new Object();
        when(callback.doInSessionWithNode(same(session), same(rootNode))).thenReturn(result);
        assertSame(result, jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback));
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getRootNode();
        verify(callback).doInSessionWithNode(same(session), same(rootNode));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
    }

    @Test
    public void withRootNodeUsingTypedCallbackShouldFailWhenBorrowSessionReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(sessionFactory.getSession(eq(TEST_WORKSPACE_NAME))).thenReturn(null);
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingTypedCallbackShouldFailWhenGetRootNodeThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(session.getRootNode()).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getRootNode();
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingTypedCallbackShouldFailWhenGetRootNodeReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(session.getRootNode()).thenReturn(null);
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getRootNode();
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withRootUsingTypedCallbackShouldFailWhenCallbackThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        doThrow(RepositoryException.class).when(callback).doInSessionWithNode(same(session), same(rootNode));
        try {
            jcrTemplate.withRoot(TEST_WORKSPACE_NAME, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getRootNode();
            verify(callback).doInSessionWithNode(same(session), same(rootNode));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathWithVoidNodeCallbackIsSuccessful() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        when(session.getNode(anyString())).thenReturn(node);
        jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getNode(eq(TEST_PATH));
        verify(callback).doInSessionWithNode(same(session), same(node));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, callback);
        verifyZeroInteractions(node);
    }

    @Test
    public void withNodePathUsingVoidCallbackShouldFailWhenBorrowSessionReturnsNullIsSuccessful() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(sessionFactory.getSession(eq(TEST_WORKSPACE_NAME))).thenReturn(null);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathUsingVoidCallbackShouldFailWhenGetRootNodeThrowsException() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(session.getNode(anyString())).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNode(eq(TEST_PATH));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathUsingVoidCallbackShouldFailWhenGetRootNodeReturnsNull() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(session.getNode(anyString())).thenThrow(PathNotFoundException.class);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNode(eq(TEST_PATH));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathUsingVoidCallbackShouldFailWhenCallbackThrowsException() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        when(session.getNode(anyString())).thenReturn(node);
        doThrow(RepositoryException.class).when(callback).doInSessionWithNode(any(Session.class), any(Node.class));
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNode(eq(TEST_PATH));
            verify(callback).doInSessionWithNode(same(session), same(node));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
            verifyZeroInteractions(node);
        }
    }

    @Test
    public void withNodePathWithTypedNodeCallbackIsSuccessful() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        final Object result = new Object();
        when(session.getNode(anyString())).thenReturn(node);
        when(callback.doInSessionWithNode(any(Session.class), any(Node.class))).thenReturn(result);
        assertSame(result, jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback));
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getNode(eq(TEST_PATH));
        verify(callback).doInSessionWithNode(same(session), same(node));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, callback);
        verifyZeroInteractions(node);
    }

    @Test
    public void withNodePathUsingTypedCallbackShouldFailWhenBorrowSessionReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(sessionFactory.getSession(eq(TEST_WORKSPACE_NAME))).thenReturn(null);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathUsingTypedCallbackShouldFailWhenGetRootNodeThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(session.getNode(anyString())).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNode(eq(TEST_PATH));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathUsingTypedCallbackShouldFailWhenGetRootNodeReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(session.getNode(anyString())).thenThrow(PathNotFoundException.class);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNode(eq(TEST_PATH));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodePathUsingTypedCallbackShouldFailWhenCallbackThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        when(session.getNode(anyString())).thenReturn(node);
        when(callback.doInSessionWithNode(any(Session.class), any(Node.class))).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodePath(TEST_WORKSPACE_NAME, TEST_PATH, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNode(eq(TEST_PATH));
            verify(callback).doInSessionWithNode(same(session), same(node));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
            verifyZeroInteractions(node);
        }
    }

    @Test
    public void withNodeIdWithVoidNodeCallbackIsSuccessful() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        when(session.getNodeByIdentifier(anyString())).thenReturn(node);
        jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getNodeByIdentifier(eq(TEST_ID));
        verify(callback).doInSessionWithNode(same(session), same(node));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, callback);
        verifyZeroInteractions(node);
    }

    @Test
    public void withNodeIdUsingVoidCallbackShouldFailWhenBorrowSessionReturnsNull() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(sessionFactory.getSession(eq(TEST_WORKSPACE_NAME))).thenReturn(null);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodeIdUsingVoidCallbackShouldFailWhenGetRootNodeThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(session.getNodeByIdentifier(anyString())).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNodeByIdentifier(eq(TEST_ID));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodeIdUsingVoidCallbackShouldFailWhenGetRootNodeReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(session.getNodeByIdentifier(anyString())).thenThrow(PathNotFoundException.class);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNodeByIdentifier(eq(TEST_ID));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodeIdUsingVoidCallbackShouldFailWhenCallbackThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        when(session.getNodeByIdentifier(anyString())).thenReturn(node);
        when(callback.doInSessionWithNode(any(Session.class), any(Node.class))).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNodeByIdentifier(eq(TEST_ID));
            verify(callback).doInSessionWithNode(same(session), same(node));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
            verifyZeroInteractions(node);
        }
    }

    @Test
    public void withNodeIdWithTypedNodeCallbackIsSuccessful() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        final Object result = new Object();
        when(session.getNodeByIdentifier(anyString())).thenReturn(node);
        when(callback.doInSessionWithNode(any(Session.class), any(Node.class))).thenReturn(result);
        assertSame(result, jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback));
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getNodeByIdentifier(eq(TEST_ID));
        verify(callback).doInSessionWithNode(same(session), same(node));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, callback);
        verifyZeroInteractions(node);
    }

    @Test
    public void withNodeIdUsingTypedCallbackShouldFailWhenBorrowSessionReturnsNull() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        when(sessionFactory.getSession(eq(TEST_WORKSPACE_NAME))).thenReturn(null);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodeIdUsingTypedCallbackShouldFailWhenGetRootNodeThrowsException() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(session.getNodeByIdentifier(anyString())).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNodeByIdentifier(eq(TEST_ID));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodeIdUsingTypedCallbackShouldFailWhenGetRootNodeReturnsNull() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        when(session.getNodeByIdentifier(anyString())).thenThrow(PathNotFoundException.class);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNodeByIdentifier(eq(TEST_ID));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
        }
    }

    @Test
    public void withNodeIdUsingTypedCallbackShouldFailWhenCallbackThrowsException() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Node node = mock(Node.class);
        when(session.getNodeByIdentifier(anyString())).thenReturn(node);
        when(callback.doInSessionWithNode(any(Session.class), any(Node.class))).thenThrow(RepositoryException.class);
        try {
            jcrTemplate.withNodeId(TEST_WORKSPACE_NAME, TEST_ID, callback);
            fail();
        } catch (final RepositoryAccessException e) {
            verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
            verify(session).getNodeByIdentifier(eq(TEST_ID));
            verify(callback).doInSessionWithNode(same(session), same(node));
            verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
            verifyNoMoreInteractions(sessionFactory, session, rootNode, callback);
            verifyZeroInteractions(node);
        }
    }

    // void execute(String workspaceName, SessionVoidCallback callback);

    @Test
    public void executeWithVoidSessionCallbackIsSuccessful() throws Exception {
        final SessionCallback<Object> callback = mock(SessionCallback.class);
        jcrTemplate.withSession(TEST_WORKSPACE_NAME, callback);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(callback).doInSession(same(session));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, callback);
        verifyZeroInteractions(session);
    }

    @Test
    public void executeWithTypedSessionCallbackIsSuccessful() throws Exception {
        final SessionCallback<Object> callback = mock(SessionCallback.class);
        jcrTemplate.withSession(TEST_WORKSPACE_NAME, callback);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(callback).doInSession(same(session));
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, callback);
        verifyZeroInteractions(session);
    }

    // <T> T withSession(String workspaceName, SessionCallback<T> callback);

    @Test
    public void executeStatementWithVoidNodeCallbackIsSuccessfulForEmptyResultset() throws Exception {
        final NodeCallback<Object>  callback = mock(NodeCallback.class);
        final Workspace workspace = mock(Workspace.class);
        final QueryManager queryManager = mock(QueryManager.class);
        final Query query = mock(Query.class);
        final QueryResult queryResult = mock(QueryResult.class);
        final NodeIterator nodeIterator = mock(NodeIterator.class);
        when(session.getWorkspace()).thenReturn(workspace);
        when(workspace.getQueryManager()).thenReturn(queryManager);
        when(queryManager.createQuery(anyString(), anyString())).thenReturn(query);
        when(query.execute()).thenReturn(queryResult);
        when(queryResult.getNodes()).thenReturn(nodeIterator);
        when(nodeIterator.hasNext()).thenReturn(false);
        jcrTemplate.withQueryResults(TEST_WORKSPACE_NAME, TEST_STATEMENT, Query.JCR_SQL2, callback, 0, 100);
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getWorkspace();
        verify(workspace).getQueryManager();
        verify(queryManager).createQuery(eq(TEST_STATEMENT), eq(Query.JCR_SQL2));
        verify(query).setOffset(eq(0L));
        verify(query).setLimit(eq(100L));
        verify(query).execute();
        verify(queryResult).getNodes();
        verify(nodeIterator).getSize();
        verify(nodeIterator).hasNext();
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, workspace, queryManager, query, queryResult, nodeIterator);
    }

    // <T> List<T> withSession(String workspaceName, String statement,
    //                    String language, NodeCallback<T> callback);

    @Test
    public void executeStatementWithTypedNodeCallbackIsSuccessfulForEmptyResultset() throws Exception {
        final NodeCallback<Object> callback = mock(NodeCallback.class);
        final Workspace workspace = mock(Workspace.class);
        final QueryManager queryManager = mock(QueryManager.class);
        final Query query = mock(Query.class);
        final QueryResult queryResult = mock(QueryResult.class);
        final NodeIterator nodeIterator = mock(NodeIterator.class);
        when(session.getWorkspace()).thenReturn(workspace);
        when(workspace.getQueryManager()).thenReturn(queryManager);
        when(queryManager.createQuery(anyString(), anyString())).thenReturn(query);
        when(query.execute()).thenReturn(queryResult);
        when(queryResult.getNodes()).thenReturn(nodeIterator);
        when(nodeIterator.getSize()).thenReturn(0L);
        when(nodeIterator.hasNext()).thenReturn(false);
        final List<Object> results = jcrTemplate.withQueryResults(TEST_WORKSPACE_NAME, TEST_STATEMENT, Query.JCR_SQL2, callback, 0, 100);
        assertNotNull(results);
        assertEquals(0, results.size());
        verify(sessionFactory).getSession(eq(TEST_WORKSPACE_NAME));
        verify(session).getWorkspace();
        verify(workspace).getQueryManager();
        verify(queryManager).createQuery(eq(TEST_STATEMENT), eq(Query.JCR_SQL2));
        verify(query).setOffset(eq(0L));
        verify(query).setLimit(eq(100L));
        verify(query).execute();
        verify(queryResult).getNodes();
        verify(nodeIterator).getSize();
        verify(nodeIterator).hasNext();
        verify(sessionFactory).releaseSession(eq(TEST_WORKSPACE_NAME), same(session));
        verifyNoMoreInteractions(sessionFactory, session, workspace, queryManager, query, queryResult, nodeIterator);
    }
}
