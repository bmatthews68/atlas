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

package com.btmatthews.atlas.tenancy.support.spring;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class TenantScope implements Scope {

    /**
     * Contains the tenant scoped beans. Tenant scoped beans are keyed by the tenant domain object identifier.
     */
    private Map<String, Map<String, Object>> scopes = new ConcurrentHashMap<String, Map<String, Object>>();

    /**
     * Lookup or create a tenant scoped bean. If a bean has to be created and it supports the {@link TenantAware}
     * interface then the tenant domain object is injected via the
     * {@link TenantAware#setTenant(com.btmatthews.atlas.tenancy.support.domain.Tenant)} method.
     *
     * @param name          The bean name.
     * @param objectFactory The factory used to create the bean if it does not already exist.
     * @return The existing or newly created bean.
     */
    @Override
    public Object get(final String name, final ObjectFactory<?> objectFactory) {
        final String tenantId = getConversationId();
        if (tenantId == null) {
            return objectFactory.getObject();
        } else {
            Map<String, Object> scope;
            if (scopes.containsKey(tenantId)) {
                scope = scopes.get(tenantId);
            } else {
                scope = new ConcurrentHashMap<String, Object>();
                scopes.put(tenantId, scope);
            }
            Object bean;
            if (scope.containsKey(name)) {
                bean = scope.get(name);
            } else {
                bean = objectFactory.getObject();
                if (bean instanceof TenantAware) {
                    ((TenantAware) bean).setTenant(TenantContextHolder.peek());
                }
                scope.put(name, bean);
            }
            return bean;
        }
    }

    /**
     * Remove a tenant scoped bean for the current application context.
     *
     * @param name The name of the tenant scoped bean.
     * @return The matching bean or {@code null} if the bean was not found in the application context.
     */
    @Override
    public Object remove(final String name) {
        final String tenantId = getConversationId();
        if (tenantId != null && scopes.containsKey(tenantId)) {
            final Map<String, Object> scope = scopes.get(tenantId);
            return scope.remove(name);
        } else {
            return null;
        }
    }

    /**
     * @param name
     * @param callback
     */
    @Override
    public void registerDestructionCallback(final String name,
                                            final Runnable callback) {

    }

    /**
     * @param key
     * @return
     */
    @Override
    public Object resolveContextualObject(final String key) {
        return null;
    }

    /**
     * Return the tenant domain object's identifier for the current thread as the conversation identifier.
     *
     * @return The tenant domain object's identifier.
     */
    @Override
    public String getConversationId() {
        final Tenant tenant = TenantContextHolder.peek();
        if (tenant != null) {
            return tenant.getId();
        } else {
            return null;
        }
    }

}