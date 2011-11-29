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

import java.util.List;

public interface JCRAccessor {

	void withRoot(String workspaceName, NodeVoidCallback callback);

	<T> T withRoot(String workspaceName, NodeCallback<T> callback);

	void withNodePath(String workspaceName, String path,
			NodeVoidCallback callback);

	<T> T withNodePath(String workspaceName, String path,
			NodeCallback<T> callback);

	void withNodeId(String workspaceName, String id, NodeVoidCallback callback);

	<T> T withNodeId(String workspaceName, String id, NodeCallback<T> callback);

	void execute(String workspaceName, SessionVoidCallback callback);

	<T> T execute(String workspaceName, SessionCallback<T> callback);

	<T> List<T> execute(String workspaceName, String statement,
			String language, NodeCallback<T> callback);
}
