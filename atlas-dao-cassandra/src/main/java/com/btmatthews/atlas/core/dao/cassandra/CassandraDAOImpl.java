package com.btmatthews.atlas.core.dao.cassandra;

import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.dao.DAO;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.List;
import java.util.Optional;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

public class CassandraDAOImpl<ID, I> implements DAO<ID, I> {

    private final CassandraTemplate cassandraTemplate;
    private final String keyspace;
    private final String table;

    public CassandraDAOImpl(final Cluster cluster,
                            final String keyspace,
                            final String table) {
        cassandraTemplate = new CassandraTemplate(cluster);
        this.keyspace = keyspace;
        this.table = table;
    }

    @Override
    public long count() {
        return cassandraTemplate.execute(session -> doCount(session));
    }

    @Override
    public List<I> find(final Paging paging) {
        return cassandraTemplate.execute(session -> doFind(session, paging));
    }

    @Override
    public Optional<I> lookup(final String key,
                    final Object value) {
          return cassandraTemplate.execute(session -> doLookup(session, key, value));
    }

    @Override
    public void create(final ID id,
                       final I entity) {
        cassandraTemplate.executeVoid(session -> doCreate(session, entity));
    }

    @Override
    public Optional<I> read(final ID id) {
        return cassandraTemplate.execute(session -> doRead(session, id));
    }

    @Override
    public List<Optional<I>> read(final ID... ids) {
        return cassandraTemplate.execute(session -> transform(asList(ids), id -> doRead(session, id)));
    }

    @Override
    public void update(final ID id,
                       final I entity) {
        cassandraTemplate.executeVoid(session -> doUpdate(session, id, entity));
    }

    @Override
    public void destroy(final ID id) {
        cassandraTemplate.executeVoid(session -> doDestroy(session, id));
    }

    private long doCount(final Session session) {
        final Statement statement = select().countAll().from(keyspace, table);
        final ResultSet results = session.execute(statement);
        final Row row = results.one();
        return row.getLong(0);
    }

    private Optional<I> doLookup(final Session session,
                       final String key,
                       final Object value) {
        final Statement statement = select().from(keyspace, table).where(eq(key, value));
        return fetchOne(session, statement);
    }

    private List<I> doFind(final Session session,
                           final Paging paging) {
        final Statement statement = select().from(keyspace, table).limit(paging.getPageSize());
        return fetchMany(session, statement);

    }

    private void doCreate(final Session session,
                          final I object) {
        final Statement statement = insertInto(keyspace, table);
        encode(statement, object);
        session.execute(statement);
    }

    private Optional<I> doRead(final Session session,
                     final ID id) {
        final Statement statement = select().from(keyspace, table).where(eq("id", id));
        return fetchOne(session, statement);
    }

    private void doUpdate(final Session session,
                          final ID id,
                          final I entity) {
        final Statement statement = QueryBuilder.update(keyspace, table).where(eq("id", id));
        encode(statement, entity);
        session.execute(statement);
    }

    private void doDestroy(final Session session,
                           final ID id) {
        final Statement statement = delete().from(keyspace, table).where(eq("id", id));
        session.execute(statement);
    }

    private Optional<I> fetchOne(final Session session,
                       final Statement statement) {
        final ResultSet results = session.execute(statement);
        final Row row = results.one();
        return Optional.of(decode(row));
    }

    private List<I> fetchMany(final Session session,
                              final Statement statement) {
        final ResultSet results = session.execute(statement);
        return transform(results.all(), row -> decode(row));
    }

    private void encode(final Statement statement,
                        final I object) {
    }

    private I decode(final Row row) {
        return null;
    }
}
