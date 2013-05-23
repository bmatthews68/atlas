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

import java.util.Stack;

/**
 * This holder is used to maintain the domain object for the current thread. This implementation uses a stack so
 * that nesting can be supported.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public abstract class TenantContextHolder {

    /**
     * The context stack is a {@link ThreadLocal}.
     */
    private static final ThreadLocal<Stack<Tenant>> TENANT_STACKS = new ThreadLocal<>();

    /**
     * Push a new {@link Tenant} onto the context stack.
     *
     * @param tenant The new {@link Tenant} being pushed onto the context stack.
     */
    public static void push(final Tenant tenant) {
        Stack<Tenant> tenantStack = TENANT_STACKS.get();
        if (tenantStack == null) {
            tenantStack = new Stack<>();
            TENANT_STACKS.set(tenantStack);
        }
        tenantStack.push(tenant);
    }

    /**
     * Read the current last {@link Tenant} from the context stack.
     *
     * @return The last {@link Tenant} on the context stack.
     */
    public static Tenant peek() {
        final Stack<Tenant> tenantStack = TENANT_STACKS.get();
        if (tenantStack == null) {
            return null;
        } else {
            return tenantStack.peek();
        }
    }

    /**
     * Pop the current last {@link Tenant} from the context stack.
     *
     * @return The last {@link Tenant} on the context stack.
     */
    public static Tenant pop() {
        final Stack<Tenant> tenantStack = TENANT_STACKS.get();
        if (tenantStack == null) {
            return null;
        } else {
            return tenantStack.pop();
        }
    }
}