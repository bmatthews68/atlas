package com.btmatthews.atlas.core.dao.cassandra;

import com.datastax.driver.core.Session;

/**
 * Created by bmatthews68 on 31/03/2014.
 */
public interface Callback<T> {

    T doWithSession(Session session);
}
