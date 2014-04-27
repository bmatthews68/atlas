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

import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.dao.DAO;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    private final String databaseName;
    private final String collectionName;
    private final JacksonDBCollection<I, ID> collection;

    /**
     * Initialise the Mongo data access object setting concrete class to
     * {@code clazz}.
     */
    public MongoDAO(final MongoClient mongoClient,
                    final Class<ID> keyClass,
                    final Class<T> objectClass,
                    final String databaseName,
                    final String collectionName) {
        if (mongoClient == null) {
            throw new IllegalArgumentException("mongoClient must not be null");
        }
        if (keyClass == null) {
            throw new IllegalArgumentException("keyClass must not be null");
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

        this.databaseName = databaseName;
        this.collectionName = collectionName;

        final DB db = mongoClient.getDB(databaseName);
        final DBCollection collection = db.getCollection(collectionName);
        this.collection = (JacksonDBCollection<I, ID>) JacksonDBCollection.wrap(collection, objectClass, keyClass);
    }

    /**
     * Count the objects of the specified type in the data store.
     *
     * @return The number of objects.
     */
    @Override
    public final long count() {
        LOGGER.debug("Count objects in collection: {}.{}", databaseName, collectionName);
        final long count = collection.count();
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
        final DBCursor<I> cursor = collection
                .find()
                .skip(paging.getPageNumber() * paging.getPageSize())
                .limit(paging.getPageSize());
        final List<I> entities = new ArrayList<>();
        while (cursor.hasNext()) {
            entities.add(cursor.next());
        }
        return entities;
    }

    @Override
    public final I lookup(final String key,
                          final Object value) {
        LOGGER.debug("Lookup object in collection: {}.{} with key: {}={}", databaseName, collectionName, key, value);
        final DBCursor<I> cursor = collection.find().is(key, value);
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            return null;
        }
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
        final WriteResult<I, ID> result = collection.insert(entity);
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
        return collection.findOneById(id);
    }

    @Override
    public final List<I> read(final ID... ids) {
        final List<I> entities = new ArrayList<>();
        for (final ID id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("ids must not contain null");
            }
            entities.add(collection.findOneById(id));
        }
        return entities;
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
        collection.updateById(id, entity);
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
        collection.removeById(id);
    }

}
