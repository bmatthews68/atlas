package com.btmatthews.atlas.tenancy.support.spring;

import com.btmatthews.atlas.tenancy.support.domain.Tenant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.1.0
 */
public class TestTenantScope {

    private static final String TENANT_ID = "3d385af0-cc11-11e2-8b8b-0800200c9a66";
    @Mock
    private Tenant tenant;
    @Mock
    private ObjectFactory<ScopedObject> objectFactory;
    @Mock
    private ScopedObject object;
    private Scope scope;

    @Before
    public void setup() {
        initMocks(this);
        scope = new TenantScope();
        when(objectFactory.getObject()).thenReturn(object);
        when(tenant.getId()).thenReturn(TENANT_ID);
    }

    @Test
    public void conversationIdShouldBeNullIfNoContextSet() {
        assertThat(scope.getConversationId(), is(nullValue()));
        verifyNoMoreInteractions(objectFactory, tenant);
    }

    @Test
    public void conversationIdShouldHaveIdIfContextSet() {
        TenantContextHolder.push(tenant);
        assertThat(scope.getConversationId(), is(equalTo(TENANT_ID)));
        TenantContextHolder.pop();
        verify(tenant).getId();
        verifyNoMoreInteractions(objectFactory, tenant);
    }

    @Test
    public void createScopedObjectWithoutTenantContext() {
        final ScopedObject result = (ScopedObject) scope.get("object", objectFactory);
        assertThat(result, is(sameInstance(object)));
        verify(objectFactory).getObject();
        verifyNoMoreInteractions(tenant, object, objectFactory);
    }

    @Test
    public void createScopedObjectWithTenantContext() {
        TenantContextHolder.push(tenant);
        final ScopedObject result = (ScopedObject) scope.get("object", objectFactory);
        assertThat(result, is(sameInstance(object)));
        TenantContextHolder.pop();
        verify(objectFactory).getObject();
        verify(tenant).getId();
        verifyNoMoreInteractions(objectFactory, tenant);
    }

    @Test
    public void createMultipleScopedObjectsWithTenantContext() {
        TenantContextHolder.push(tenant);
        ScopedObject result = (ScopedObject) scope.get("object", objectFactory);
        assertThat(result, is(sameInstance(object)));
        result = (ScopedObject) scope.get("object", objectFactory);
        assertThat(result, is(sameInstance(object)));
        TenantContextHolder.pop();
        verify(objectFactory, times(1)).getObject();
        verify(tenant, times(2)).getId();
        verifyNoMoreInteractions(objectFactory, tenant);
    }

    @Test
    public void resolveContextualObjectAlwaysReturnsNull() {
        assertThat(scope.resolveContextualObject(""), is(nullValue()));
    }

    @Test
    public void registerDestructionCallbackDoesNothing() {
        scope.registerDestructionCallback("", null);
    }
}
