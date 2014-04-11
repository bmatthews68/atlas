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

package com.btmatthews.atlas.jcr;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public interface JCRAccessor {

    boolean hasProperty(Node node, String name);

    boolean hasProperties(Node node, String... names);

    Binary getBinaryProperty(Node node, String name);

    Binary getBinaryProperty(Node node, String name, Binary defaultValue);

    BigDecimal getBigDecimalProperty(Node node, String name);

    BigDecimal getBigDecimalProperty(Node node, String name, BigDecimal defaultValue);

    Boolean getBooleanProperty(Node node, String name);

    Boolean getBooleanProperty(Node node, String name, Boolean defaultValue);

    Calendar getCalendarProperty(Node node, String name);

    Calendar getCalendarProperty(Node node, String name, Calendar defaultValue);

    Double getDoubleProperty(Node node, String name);

    Double getDoubleProperty(Node node, String name, Double defaultValue);

    Long getLongProperty(Node node, String name);

    Long getLongProperty(Node node, String name, Long defaultValue);

    String getPathProperty(Node node, String name);

    String getPathProperty(Node node, String name, String defaultValue);

    Node getReferenceProperty(Node node, String name);

    Node getReferenceProperty(Node node, String name, Node defaultValue);

    String getStringProperty(Node node, String name);

    String getStringProperty(Node node, String name, String defaultValue);

    URI getURIProperty(Node node, String name);

    URI getURIProperty(Node node, String name, URI defaultValue);

    Node getOrCreateNode(Node node, String leafType, String name);

    Node getOrCreateNode(Node node, String intermediateType, String leafType, String path);

    Node getOrCreateNode(Node node, String intermediateType, String leafType, String... names);

    void withRoot(String workspaceName, NodeVoidCallback callback);

    <T> T withRoot(String workspaceName, NodeCallback<T> callback);

    void withNodePath(String workspaceName, String path,
                      NodeVoidCallback callback);

    <T> T withNodePath(String workspaceName, String path,
                       NodeCallback<T> callback);

    void withNodeId(String workspaceName, String id, NodeVoidCallback callback);

    <T> T withNodeId(String workspaceName, String id, NodeCallback<T> callback);

    void withSession(String workspaceName, SessionVoidCallback callback);

    <T> T withSession(String workspaceName, SessionCallback<T> callback);

    void withQueryResults(String workspaceName, String statement,
                          String language, NodeVoidCallback callback, long offset, long limit);

    <T> List<T> withQueryResults(String workspaceName, String statement,
                                 String language, NodeCallback<T> callback, long offset, long limit);
}
