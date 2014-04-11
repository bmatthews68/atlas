package com.btmatthews.atlas.core.dao.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Created by bmatthews68 on 31/03/2014.
 */
public class CassandraTemplate {

    private Cluster cluster;

    public CassandraTemplate(final Cluster cluster) {
        this.cluster = cluster;
    }

    public void executeVoid(final VoidCallback callback) {
        final Session session = cluster.connect();
        if (session != null) {
            try {
                callback.doWithSession(session);
            } finally {
                session.close();
            }
        }
    }

    public <T> T execute(final Callback<T> callback) {
        final Session session = cluster.connect();
        if (session != null) {
            try {
                return callback.doWithSession(session);
            } finally {
                session.close();
            }
        }
        return null;
    }
}
