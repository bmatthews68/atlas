package com.btmatthews.atlas.jcr.servlet;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 25/04/2013
 * Time: 08:34
 * To change this template use File | Settings | File Templates.
 */
public class RequestCredentialsProvider implements CredentialsProvider {

    private Credentials globalCredentials;

    public RequestCredentialsProvider(final Credentials credentials) {
        globalCredentials = credentials;
    }

    @Override
    public Credentials getGlobalCredentials() {
        return globalCredentials;
    }

    @Override
    public Credentials getUserCredentials() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = requestAttributes.getRequest();
        final Principal principal = request.getUserPrincipal();
        return new SimpleCredentials(principal.getName(), new char[0]);
    }
}
