/*
 * Copyright 2013 Brian Matthews
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

package com.btmatthews.atlas.tenancy.support.service;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface TenantService {

    /**
     * Lookup the tenant domain object using the tenant domain object identifier.
     *
     * @param id The tenant domain object identifier.
     * @return The matching tenant domain object.
     */
    Tenant lookupTenantById(String id);

    /**
     * Lookup the tenant domain object using the tenant key.
     *
     * @param key The tenant key.
     * @return The matching tenant domain object.
     */
    Tenant lookupTenantByKey(String key);
}