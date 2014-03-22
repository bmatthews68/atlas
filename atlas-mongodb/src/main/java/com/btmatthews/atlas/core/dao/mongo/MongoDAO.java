/*
 * Copyright 2011-2013 Brian Thomas Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.atlas.core.dao.mongo;

import com.btmatthews.atlas.core.common.Ordering;
import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.common.SortDirection;
import com.btmatthews.atlas.core.dao.DAO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class that implements common features of data access objects that
 * use MongoDB for persistence.
 *
 * @param <ID> The identifier class.
 * @param <I>  The interface class.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 */
public abstract class MongoDAO<ID, I> implements DAO<ID, I> {

    private final MongoClient mongoClient;

    private final String databaseName;

    private final String collectionName;

    /**
     * Initialise the Mongo data access object setting concrete class to
     * {@code clazz}.
     */
    protected MongoDAO(final MongoClient mongoClient,
                       final String databaseName,
                       final String collectionName) {
        if (mongoClient == null) {
            throw new IllegalArgumentException("mongoClient must not be null");
        }
        if (databaseName == null && databaseName.length() > 0) {
            throw new IllegalArgumentException("databseName must not be null or empty");
        }
        if (collectionName == null && collectionName.length() > 0) {
            throw new IllegalArgumentException("collectionName must not be null or empty");
        }
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    /**
     * Count the objects of the specified type in the data store.
     *
     * @return The number of objects.
     */
    @Override
    public long count() {
        return getCollection().count();
    }

    /**
     * Retrieve a subset of the matching objects of the specified type from the data store.
     *
     * @param paging Describes the portion of the result set to return.
     * @return The subset of the matching objects.
     */
    @Override
    public List<I> find(final Paging paging) {
        if (paging == null) {
            throw new IllegalArgumentException("paging must not be null");
        }
        return readMany(new BasicDBObject(), paging);
    }

    /**
     * Persist a newly created object.
     *
     * @param entity The newly created object.
     */
    @Override
    public void create(final ID id, final I entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        final DBObject dbObject = new BasicDBObject();
        marshal(dbObject, entity);
        final WriteResult result = getCollection().save(dbObject);
        if (!result.getLastError().ok()) {
        }
    }

    /**
     * Retrieve a persisted object using its identified.
     *
     * @param id The identifier.
     * @return The matching object.
     */
    @Override
    public I read(final ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        final DBObject query = new BasicDBObject("_id", id.toString());
        return readOne(query);
    }

    @Override
    public List<I> read(final ID... ids) {
        final DBObject query = new BasicDBObject();
        final List<String> idsList = Lists.transform(Arrays.asList(ids), new Function<ID, String>() {
            @Override
            public String apply(final ID id) {
                if (id == null) {
                    throw new IllegalArgumentException("ids must not contain null");
                }
                return id.toString();
            }
        });
        query.put("_id", new BasicDBObject("$in", idsList));
        return readMany(query);
    }

    /**
     * Update an existing object in the data store.
     *
     * @param id     The persistent object identifier.
     * @param entity The existing object.
     */
    @Override
    public void update(final ID id, final I entity) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        final DBObject object = new BasicDBObject();
        marshal(object, entity);
        final WriteResult result = getCollection().update(new BasicDBObject("_id", id.toString()), object);
    }

    /**
     * Delete a object from the data store.
     *
     * @param id The persistent object identifier.
     */
    @Override
    public void destroy(final ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        final WriteResult result = getCollection().remove(new BasicDBObject("_id", id.toString()));
    }

    /**
     * Retrieve an object from the database that matches the specified query.
     *
     * @param query The query.
     * @return The matching object.
     */
    protected final I readOne(final DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        final DBObject dbObject = getCollection().findOne(query);
        if (dbObject == null) {
            return null;
        }
        return unmarshal(dbObject);
    }

    protected final List<I> readMany(final DBObject query) {
        return readMany(query, new Paging(0, Integer.MAX_VALUE));
    }

    /**
     * Execute a query that should return zero or more objects.
     *
     * @param query The query.
     * @return The matching objects.
     */
    protected final List<I> readMany(final DBObject query,
                                     final Paging paging) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        final DBCursor cursor = getCollection().find(query);
        if (paging.getPageSize() != Integer.MAX_VALUE) {
            cursor.skip(paging.getPageNumber() * paging.getPageSize())
                    .limit(paging.getPageSize());
        }
        if (paging.getSortOrderings().size() > 0) {
            final DBObject sortObject = new BasicDBObject();
            for (final Ordering ordering : paging.getSortOrderings()) {
                if (ordering.getSortDirection() == SortDirection.ASCENDING) {
                    sortObject.put(ordering.getSortField(), 1);
                } else {
                    sortObject.put(ordering.getSortField(), -1);
                }
            }
            cursor.sort(sortObject);
        }
        final List<I> objects = new ArrayList<I>();
        while (cursor.hasNext()) {
            final DBObject dbObject = cursor.next();
            final I object = unmarshal(dbObject);
            objects.add(object);
        }
        return objects;
    }

    protected final DBCollection getCollection() {
        final DB db = mongoClient.getDB(databaseName);
        return db.getCollection(collectionName);
    }

    protected abstract void marshal(DBObject dbObject, I object);

    protected abstract I unmarshal(DBObject dbObject);
}
