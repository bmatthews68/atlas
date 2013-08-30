package com.btmatthews.atlas.tenancy.support.filter;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;
import com.btmatthews.atlas.tenancy.support.service.TenantService;
import com.btmatthews.atlas.tenancy.support.spring.TenantContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.1.0
 */
public class TestTenantFilter {

    @Mock
    private Tenant tenant;
    @Mock
    private TenantService service;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private FilterChain chain;
    private Filter filter;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        final TenantFilter filterImpl = new TenantFilter();
        filterImpl.setTenantService(service);
        filterImpl.setHeaderName("X-Atlas-Tenant");
        filter = filterImpl;
        filter.init(filterConfig);
    }

    @After
    public void teardown() {
        filter.destroy();
    }

    @Test
    public void shouldSetTenantContextWhenTenantHeaderSet() throws Exception {
        when(request.getHeader(eq("X-Atlas-Tenant"))).thenReturn("shop");
        when(service.lookupTenantByKey(anyString())).thenReturn(tenant);
        filter.doFilter(request, response, chain);
        assertThat(TenantContextHolder.peek(), is(nullValue()));
        verify(request).getHeader(eq("X-Atlas-Tenant"));
        verify(service).lookupTenantByKey(eq("shop"));
        verify(chain).doFilter(same(request), same(response));
        verifyNoMoreInteractions(service, request, response, tenant, chain);
    }
}
