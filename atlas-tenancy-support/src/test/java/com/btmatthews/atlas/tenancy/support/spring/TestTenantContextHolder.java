package com.btmatthews.atlas.tenancy.support.spring;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.1.0
 */
public class TestTenantContextHolder {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Mock
    private Tenant tenant;

    @Mock
    private Tenant anotherTenant;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void popWhenEmptyShouldReturnNull() {
        assertThat(TenantContextHolder.pop(), is(nullValue()));
    }

    @Test
    public void peekWhenEmptyShouldReturnNull() {
        assertThat(TenantContextHolder.peek(), is(nullValue()));
    }

    @Test
    public void pushSingleItemOntoEmptyStack() {
        TenantContextHolder.push(tenant);
        collector.checkThat(TenantContextHolder.peek(), is(sameInstance(tenant)));
        collector.checkThat(TenantContextHolder.pop(), is(sameInstance(tenant)));
        collector.checkThat(TenantContextHolder.peek(), is(nullValue()));
        collector.checkThat(TenantContextHolder.pop(), is(nullValue()));
    }

    @Test
    public void pushMultipleItemsOntoEmptyStack() {
        TenantContextHolder.push(tenant);
        collector.checkThat(TenantContextHolder.peek(), is(sameInstance(tenant)));
        TenantContextHolder.push(anotherTenant);
        collector.checkThat(TenantContextHolder.peek(), is(sameInstance(anotherTenant)));
        collector.checkThat(TenantContextHolder.pop(), is(sameInstance(anotherTenant)));
        collector.checkThat(TenantContextHolder.peek(), is(sameInstance(tenant)));
        collector.checkThat(TenantContextHolder.pop(), is(sameInstance(tenant)));
        collector.checkThat(TenantContextHolder.peek(), is(nullValue()));
        collector.checkThat(TenantContextHolder.pop(), is(nullValue()));
    }

}
