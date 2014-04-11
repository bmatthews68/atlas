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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class that implements common features of data access objects that
 * use MongoDB for persistence.
 *
 * @param <I> The interface class.
 * @param <T> The concrete class.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 */
public abstract class MongoDAO<ID, I, T extends I> implements DAO<ID, I> {

    private final MongoClient mongoClient;
    /**
     * The concrete class.
     */
    private final Class<T> clazz;

    private final String databaseName;

    private final String collectionName;

    private MongoDbFactory dbFactory;

    /**
     * The {@link MongoTemplate} is used to interface with the underlying
     * MongoDB database.
     */
    private MongoTemplate mongoTemplate;

    /**
     * Initialise the Mongo data access object setting concrete class to
     * {@code clazz}.
     *
     * @param clazz The concrete class.
     */
    protected MongoDAO(final MongoClient mongoClient,
                       final Class<T> clazz,
                       final String databaseName,
                       final String collectionName) {
        if (mongoClient == null) {
            throw new IllegalArgumentException("mongoClient must not be null");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (databaseName == null && databaseName.length() > 0) {
            throw new IllegalArgumentException("databseName must not be null or empty");
        }
        if (collectionName == null && collectionName.length() > 0) {
            throw new IllegalArgumentException("collectionName must not be null or empty");
        }
        this.mongoClient = mongoClient;
        this.clazz = clazz;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    /**
     * Used by the Spring Framework to inject factory used to construct the
     * Mongo Template.
     *
     * @param factory A {@link MongoDbFactory}.
     */
    @Autowired
    public void setMongoDbFactory(final MongoDbFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null");
        }
        dbFactory = factory;
        mongoTemplate = new MongoTemplate(factory);
    }

    /**
     * Count the objects of the specified type in the data store.
     *
     * @return The number of objects.
     */
    @Override
    public long count() {
        return mongoTemplate.count(new Query(), clazz);
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
        final Query query = new Query();
        return find(query, paging);
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
        final DB db = mongoClient.getDB(databaseName);
        final DBCollection collection = db.getCollection(collectionName);
        final dbFactory.getDb(databaseName);
        mongoTemplate.insert(entity);
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
        return mongoTemplate.findById(id, clazz);
    }

    @Override
    public List<I> read(final ID... ids) {
        final DBObject query = new BasicDBObject();
        final List<String> idsList = new ArrayList<String>();
        for (final ID id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("ids must not contain null");
            }
            idsList.add(id.toString());
        }
        query.put("_id", new BasicDBObject("$in", idsList));
        return read(query);
    }

    /**
     * Retrieve an object from the database that matches the specified query.
     *
     * @param query The query.
     * @return The matching object.
     */
    protected final I read(final DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        final DB db = mongoClient.getDB(databaseName);
        final DBCollection collection = db.getCollection(collectionName);
        return mongoTemplate.findOne(query, clazz);
    }

    private final List<I> read(final DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        final List<I> idsList = new ArrayList<I>();
        final DB db = mongoClient.getDB(databaseName);
        final DBCollection collection = db.getCollection(collectionName);
        final DBCursor cursor =  collection.find(query);
        while (cursor.hasNext()) {
            final DBObject object = cursor.next();
        }
        return idsList;
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
        final DB db = mongoClient.getDB(databaseName);
        final DBCollection collection = db.getCollection(collectionName);
        final DBObject object = new BasicDBObject();
        final WriteResult result = collection.update(new BasicDBObject("_id", id.toString()), object);
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
        final DB db = mongoClient.getDB(databaseName);
        final DBCollection collection = db.getCollection(collectionName);
        final WriteResult result = collection.remove(new BasicDBObject("_id", id.toString()));
    }

    /**
     * Execute a query that should a return single object from the data store.
     *
     * @param query The query.
     * @return The matching object.
     */
    protected final I findOne(final Query query) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        return mongoTemplate.findOne(query, clazz);
    }

    /**
     * Execute a query that should return zero or more objects.
     *
     * @param query The query.
     * @return The matching objects.
     */
    protected final List<I> find(final Query query, final Paging paging) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        if (paging == null) {
            throw new IllegalArgumentException("paging must not be null");
        }
        query.skip(paging.getPageNumber() * paging.getPageSize());
        query.limit(paging.getPageSize());
        for (final Ordering ordering : paging.getSortOrderings()) {
            if (ordering.getSortDirection() == SortDirection.ASCENDING) {
                query.with(new Sort(Sort.Direction.ASC, ordering.getSortField()));
            } else {
                query.with(new Sort(Sort.Direction.DESC, ordering.getSortField()));
            }
        }
        return Lists.transform(mongoTemplate.find(query, clazz),
                new Function<T, I>() {
                    @Override
                    public T apply(final T input) {
                        return input;
                    }
                }
        );
    }

    protected abstract DBObject marshal(I object);

    protected abstract I unmarshal(DBObject dbObject);
}
