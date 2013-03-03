package com.btmatthews.atlas.opencmis.servlet;

import com.btmatthews.atlas.opencmis.SessionHolder;
import org.apache.chemistry.opencmis.client.api.Session;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class OpenSessionInViewFilter implements Filter {
    public void init(final FilterConfig config) throws ServletException {
    }

    public void doFilter(final ServletRequest request,
                         final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } finally {
            SessionHolder.clearSession();
        }
    }

    public void destroy() {
    }
}
