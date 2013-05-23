package com.btmatthews.atlas.jcr.impl;

import com.btmatthews.atlas.jcr.RepositoryAccessException;
import com.btmatthews.atlas.jcr.RepositoryProvider;
import org.springframework.jndi.JndiTemplate;

import javax.jcr.Repository;
import javax.naming.NamingException;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 06/05/2013
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class JNDIRepositoryProvider implements RepositoryProvider {


    private static final String DEFAULT_JNDI_NAME = "jcr/default";
    private String jndiName = DEFAULT_JNDI_NAME;

    public Repository getRepository() {
        try {
            return new JndiTemplate().lookup(jndiName, Repository.class);
        } catch (final NamingException e) {
            throw new RepositoryAccessException("Lookup JCR repository using JNDI failed", e);
        }
    }
}
