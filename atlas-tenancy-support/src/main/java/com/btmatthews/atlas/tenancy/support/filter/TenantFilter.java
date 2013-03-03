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

package com.btmatthews.atlas.tenancy.support.filter;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;
import com.btmatthews.atlas.tenancy.support.service.TenantService;
import com.btmatthews.atlas.tenancy.support.spring.TenantContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public final class TenantFilter extends OncePerRequestFilter {

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
     * Used to inject the name of the HTTP header containing the tenant key.
     *
     * @param name The name of the HTTP header containing the tenant key.
     */
    @Value("tenantRequestHeaderName")
    public void setHeaderName(final String name) {
        headerName = name;
    }

    /**
     * Used to inject the tenant service.
     *
     * @param service The tenant service.
     */
    @Autowired
    public void setTenantService(final TenantService service) {
        tenantService = service;
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
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain)
            throws ServletException, IOException {
        final String headerValue = request.getHeader(headerName);
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
    }
}