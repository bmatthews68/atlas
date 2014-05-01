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

package com.btmatthews.atlas.core.dao;

import com.btmatthews.atlas.core.common.Paging;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Describes the interface for data access objects that persist entities of
 * described by the interface {@code I} to a data store.
 *
 * @param <ID> The identifier type.
 * @param <I>  The interface that describes the persistent entity.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public interface DAO<ID, I> {

    /**
     * Count the number of entities described by {@code I} in the data source.
     *
     * @return The number of entities.
     */
    default long count() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve a portion of the ordered entities described by interface {@code I} from the data store.
     *
     * @param paging Describes the portion of the result set to return.
     * @return An ordered list of {@code I} entities.
     */
    default List<I> find(final Paging paging) {
        throw new UnsupportedOperationException();
    }

    default Optional<I> lookup(final String key,
                               final Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Persist a newly created entity in the data store.
     *
     * @param id     The object identifier of the persistent entity.
     * @param entity The newly created entity.
     */
    default void create(final ID id, final I entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve a persistent entity from the data store.
     *
     * @param id The object identifier of the persistent entity.
     * @return The persistent entity.
     */
    default Optional<I> read(final ID id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve a list of persistent entities from the data store.
     *
     * @param ids The object identifiers of the persistent entities.
     * @return An ordered list of {@code I} entities.
     */
    default List<Optional<I>> read(final ID... ids) {
        final List<Optional<I>> entities = new ArrayList<>(ids.length);
        for (final ID id : ids) {
            entities.add(read(id));
        }
        return entities;
    }

    /**
     * Update a persistent entity in the data store.
     *
     * @param id     The object identifier of the persistent entity.
     * @param entity The persistent entity.
     */
    default void update(final ID id, final I entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Delete a persistent entity from the data store.
     *
     * @param id The object identifier of the persistent entity.
     */
    default void destroy(final ID id) {
        throw new UnsupportedOperationException();
    }
}
