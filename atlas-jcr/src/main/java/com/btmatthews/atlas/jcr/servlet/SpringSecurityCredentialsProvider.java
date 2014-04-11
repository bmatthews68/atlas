package com.btmatthews.atlas.jcr.servlet;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 27/04/2013
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class SpringSecurityCredentialsProvider implements CredentialsProvider {

    private Credentials globalCredentials;

    public SpringSecurityCredentialsProvider(final Credentials credentials) {
        globalCredentials = credentials;
    }

    public Credentials getGlobalCredentials() {
        return globalCredentials;
    }

    public Credentials getUserCredentials() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        final Object principal = authentication.getPrincipal();
        return new SimpleCredentials(principal.toString(), new char[0]);
    }
}
