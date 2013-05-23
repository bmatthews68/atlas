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

import javax.jcr.Node;
import javax.jcr.Session;

/**
 * A callback used to events for individual repository nodes.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface NodeVoidCallback {

    /**
     * The method that handles the callback event.
     *
     * @param session The session.
     * @param node    The node.
     * @throws Exception If there was an error handling the callback event.
     */
    void doInSessionWithNode(Session session, Node node)
            throws Exception;
}
