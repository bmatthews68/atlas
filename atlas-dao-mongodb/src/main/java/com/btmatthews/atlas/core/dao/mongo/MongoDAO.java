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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
public class MongoDAO<ID, I, T extends I> implements DAO<ID, I> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDAO.class);

    private final MongoClient mongoClient;

    private final ObjectMapper objectMapper;

    private final String databaseName;

    private final String collectionName;

    private final Class<T> objectClass;

    /**
     * Initialise the Mongo data access object setting concrete class to
     * {@code clazz}.
     */
    public MongoDAO(final MongoClient mongoClient,
                    final ObjectMapper objectMapper,
                    final Class<T> objectClass,
                    final String databaseName,
                    final String collectionName) {
        if (mongoClient == null) {
            throw new IllegalArgumentException("mongoClient must not be null");
        }
        if (objectMapper == null) {
            throw new IllegalArgumentException("objectMapper must not be null");
        }
        if (objectClass == null) {
            throw new IllegalArgumentException("objectClass must not be null");
        }
        if (databaseName == null && databaseName.length() > 0) {
            throw new IllegalArgumentException("databseName must not be null or empty");
        }
        if (collectionName == null && collectionName.length() > 0) {
            throw new IllegalArgumentException("collectionName must not be null or empty");
        }
        this.mongoClient = mongoClient;
        this.objectMapper = objectMapper;
        this.objectClass = objectClass;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    /**
     * Count the objects of the specified type in the data store.
     *
     * @return The number of objects.
     */
    @Override
    public final long count() {
        LOGGER.debug("Count objects in collection: {}.{}", databaseName, collectionName);
        final long count = getCollection().count();
        LOGGER.debug("Counted {} objects in collection: {}.{}", count, databaseName, collectionName);
        return count;
    }

    /**
     * Retrieve a subset of the matching objects of the specified type from the data store.
     *
     * @param paging Describes the portion of the result set to return.
     * @return The subset of the matching objects.
     */
    @Override
    public final List<I> find(final Paging paging) {
        LOGGER.debug("Find object in collection: {}.{}", databaseName, collectionName);
        if (paging == null) {
            throw new IllegalArgumentException("paging must not be null");
        }
        return readMany(new BasicDBObject(), paging);
    }

    @Override
    public final I lookup(final String key,
                          final Object value) {
        LOGGER.debug("Lookup object in collection: {}.{} with key: {}={}", databaseName, collectionName, key, value);
        final DBObject query = new BasicDBObject(key, value);
        return readOne(query);
    }

    /**
     * Persist a newly created object.
     *
     * @param entity The newly created object.
     */
    @Override
    public final void create(final ID id,
                             final I entity) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        LOGGER.debug("Create object in collection: {}.{} with id: {}", databaseName, collectionName, id);
        final DBObject dbObject = marshal(entity);
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
    public final I read(final ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        LOGGER.debug("Read object from collection: {}.{} with id: {}", databaseName, collectionName, id);
        final DBObject query = new BasicDBObject("_id", id.toString());
        return readOne(query);
    }

    @Override
    public final List<I> read(final ID... ids) {
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
    public final void update(final ID id,
                             final I entity) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        LOGGER.debug("Update object in collection: {}.{} with id: {}", databaseName, collectionName, id);
        final DBObject object = marshal(entity);
        final WriteResult result = getCollection().update(new BasicDBObject("_id", id.toString()), object);
    }

    /**
     * Delete a object from the data store.
     *
     * @param id The persistent object identifier.
     */
    @Override
    public final void destroy(final ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        LOGGER.debug("Delete object from collection: {}.{} with id: {}", databaseName, collectionName, id);
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
        LOGGER.debug("Query single object from collection: {}.{}", databaseName, collectionName);
        final DBObject dbObject = getCollection().findOne(query);
        if (dbObject == null) {
            return null;
        }
        return unmarshal(dbObject);
    }

    protected final List<I> readMany(final DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
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
        if (paging == null) {
            throw new IllegalArgumentException("paging must not be null");
        }
        LOGGER.debug("Query multiple objects from collection: {}.{}", databaseName, collectionName);
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
            if (object != null) {
                objects.add(object);
            }
        }
        return objects;
    }

    private DBCollection getCollection() {
        final DB db = mongoClient.getDB(databaseName);
        return db.getCollection(collectionName);
    }

    private DBObject marshal(final I object) {
        if (object == null) {
            throw new IllegalArgumentException("object must not be null");
        }
        return new DBObjectEncoder().encode(object);
       /* try {
            final String json = objectMapper.writeValueAsString(object);
            LOGGER.debug("Marshalling: {}", json);
            return (DBObject) JSON.parse(json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting JSON string to DBObject", e);
            return null;
        }  */
    }

    private I unmarshal(final DBObject dbObject) {
        if (dbObject == null) {
            throw new IllegalArgumentException("dbObject must not be null");
        }
        try {
            final String json = JSON.serialize(dbObject);
            LOGGER.debug("Unmarshalling: {}", json);
            return objectMapper.readValue(json, objectClass);
        } catch (final IOException e) {
            LOGGER.error("Error converting JSON string to entity", e);
            return null;
        }
    }
}
