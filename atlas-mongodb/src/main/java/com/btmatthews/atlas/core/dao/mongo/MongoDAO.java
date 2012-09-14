/*
 * Copyright 2011-2012 Brian Matthews
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

import java.util.List;

import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.common.Paging.SortDirection;
import com.btmatthews.atlas.core.dao.DAO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @param <I> The interface class.
 * @param <T> The concrete class.
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 */
public abstract class MongoDAO<I, T extends I> implements DAO<I> {

    /**
     * The concrete class.
     */
    private Class<T> clazz;

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
    protected MongoDAO(final Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Used by the Spring Framework to inject factory used to construct the
     * Mongo Template.
     *
     * @param factory A {@link MongoDbFactory}.
     */
    @Autowired
    public void setMongoDbFactory(final MongoDbFactory factory) {
        mongoTemplate = new MongoTemplate(factory);
    }

    @Override
    public long count() {
        return mongoTemplate.count(new Query(), clazz);
    }

    @Override
    public List<I> find(final Paging paging) {
        final Query query = new Query();
        return find(query, paging);
    }

    /**
     * Persist a newly created object.
     *
     * @param entity The newly created object.
     */
    @Override
    public void create(final I entity) {
        mongoTemplate.insert(entity);
    }

    /**
     * Retrieve a persisted object using its identified.
     *
     * @param id The identifier.
     * @return The matching object.
     */
    @Override
    public I read(final String id) {
        return mongoTemplate.findById(id, clazz);
    }

    protected final I read(final Query query) {
        return mongoTemplate.findOne(query, clazz);
    }

    @Override
    public void update(final I entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public void destroy(final I entity) {
        mongoTemplate.remove(entity);
    }

    /**
     * Execute a query that should a single object.
     *
     * @param query The query.
     * @return The matching object.
     */
    protected final I findOne(final Query query) {
        return mongoTemplate.findOne(query, clazz);
    }

    /**
     * Execute a query that should return zero or more objects.
     *
     * @param query The query.
     * @return The matching objects.
     */
    protected final List<I> find(final Query query, final Paging paging) {
        query.skip(paging.getPageNumber() * paging.getPageSize());
        query.limit(paging.getPageSize());
        for (final Paging.Ordering ordering : paging.getSortOrderings()) {
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
                });
    }
}
