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

package com.btmatthews.atlas.tenancy.support.filter;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;
import com.btmatthews.atlas.tenancy.support.service.TenantService;
import com.btmatthews.atlas.tenancy.support.spring.TenantContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.1.0
 */
public final class TenantFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantFilter.class);
    /**
     * The name of the HTTP header containing the tenant key.
     */
    private String headerName;
    /**
     * The tenant service.
     */
    private TenantService tenantService;


    /**
     * Initialize the filter with the tenant service.
     *
     * @param service The tenant service.
     */
    public TenantFilter(final TenantService service) {
        this(service, "X-Atlas-Tenant");
    }

    /**
     * Initialize the filter with the tenant service and the header name.
     *
     * @param service The tenant service.
     * @param name    The name of the HTTP header containing the tenant key.
     */
    public TenantFilter(final TenantService service,
                        final String name) {
        tenantService = service;
        headerName = name;
    }

    @Override
    public void init(final FilterConfig config) {
    }

    /**
     * Examine the HTTP request headers and if the tenant key header (see {@link #headerName}) is present then lookup
     * the tenant domain object using the tenant key and store it in the {@link TenantContextHolder} for used by
     * tenant scoped beans.
     *
     * @param request  The HTTP request being processed.
     * @param response The HTTP response being processed.
     * @param chain    Used to invoke the next filter in the filter chain.
     * @throws ServletException If there was a servlet exception processing the HTTP request.
     * @throws IOException      If there was an I/O exception processing the HTTP request.
     */
    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain)
            throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            final String headerValue = ((HttpServletRequest) request).getHeader(headerName);
            if (headerValue != null) {
                final Tenant tenant = tenantService.lookupTenantByKey(headerValue);
                if (tenant != null) {
                    LOGGER.debug("Tenant being set for tenant key {}", headerValue);
                    TenantContextHolder.push(tenant);
                    try {
                        chain.doFilter(request, response);
                    } finally {
                        TenantContextHolder.pop();
                    }
                } else {
                    LOGGER.warn("Tenant not being set because tenant with key {} not found", headerValue);
                    chain.doFilter(request, response);
                }
            } else {
                LOGGER.warn("Tenant not being set because {} header not present", headerValue);
                chain.doFilter(request, response);
            }
        } else {
            LOGGER.warn("Tenant not being set because the request is not a HTTP request");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}