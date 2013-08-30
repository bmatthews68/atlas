/*
 * Copyright 2013 Brian Thomas Matthews
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

package com.btmatthews.atlas.tenancy.support.domain;

/**
 * The interface that defines the attributes of a tenant domain object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.1.0
 */
public interface Tenant {

    /**
     * Get the identifier for the tenant domain object.
     *
     * @return The identifier for the tenant domain object.
     */
    String getId();

    /**
     * Get the key for the tenant domain object.
     *
     * @return The key for the tenant domain object.
     */
    String getKey();
}