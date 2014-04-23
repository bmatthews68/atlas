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

package com.btmatthews.atlas.core.common;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder for creating and modifying {@link Paging} objects.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class PagingBuilder {

    /**
     * The page number used when constructing the {@link Paging} object.
     */
    private int pageNumber = 0;
    /**
     * The page size used when constructing the {@link Paging} object.
     */
    private int pageSize = 1;
    /**
     * The sort orderings used when constructing the {@link Paging} object.
     */
    private List<Ordering> sortOrderings = new ArrayList<>();

    /**
     * The constructor used when we need to create a new {@link Paging} object from scratch.
     */
    public PagingBuilder() {
    }

    /**
     * The constructor used when we need to modify an existing {@link Paging} object.
     *
     * @param paging The paging object.
     */
    public PagingBuilder(final Paging paging) {
        pageNumber = paging.getPageNumber();
        pageSize = paging.getPageSize();
        sortOrderings.addAll(paging.getSortOrderings());
    }

    /**
     * Construct a predicate that can be used to find a sort ordering by the field name.
     *
     * @param name The sort field name.
     * @return A {@link Predicate} object.
     */
    private static Predicate<Ordering> findOrdering(final String name) {
        return new Predicate<Ordering>() {
            @Override
            public boolean apply(final Ordering ordering) {
                return ordering.getSortField().equals(name);
            }
        };
    }

    /**
     * Set the page number.
     *
     * @param number The page number.
     * @return Always returns the {@link PagingBuilder object}.
     */
    public PagingBuilder setPageNumber(final int number) {
        pageNumber = number;
        return this;
    }

    /**
     * Set the page size.
     *
     * @param size The page size.
     * @return Always returns the {@link PagingBuilder} object.
     */
    public PagingBuilder setPageSize(final int size) {
        pageSize = size;
        return this;
    }

    /**
     * Promote a sort ordering moving it up one position.
     *
     * @param name The sort field name.
     * @return Always returns the {@link PagingBuilder} object.
     */
    public PagingBuilder promoteOrdering(final String name) {
        final int index = Iterables.indexOf(sortOrderings, findOrdering(name));
        if (index > 0) {
            final Ordering temp = sortOrderings.get(index - 1);
            sortOrderings.set(index - 1, sortOrderings.get(index));
            sortOrderings.set(index, temp);
        }
        return this;
    }

    /**
     * Demote a sort ordering moving it down one position.
     *
     * @param name The sort field name.
     * @return Always returns the {@link PagingBuilder} object.
     */
    public PagingBuilder demoteOrdering(final String name) {
        final int index = Iterables.indexOf(sortOrderings, findOrdering(name));
        if (index >= 0 && index < sortOrderings.size() - 1) {
            final Ordering temp = sortOrderings.get(index + 1);
            sortOrderings.set(index + 1, sortOrderings.get(index));
            sortOrderings.set(index, temp);
        }
        return this;
    }

    /**
     * Add a sort ordering.
     *
     * @param name The sort field name.
     * @param direction The sort field direction.
     * @return Always returns the {@link PagingBuilder} object.
     */
    public PagingBuilder addOrdering(final String name, final SortDirection direction) {
        final Ordering ordering = new Ordering(name, direction);
        sortOrderings.add(ordering);
        return this;
    }

    /**
     * Toggle the direction of the sort ordering.
     *
     * @param field The field name.
     * @return Always returns the {@link PagingBuilder} object.
     */
    public PagingBuilder toggleOrdering(final String field) {
        final int index = Iterables.indexOf(sortOrderings, findOrdering(field));
        if (index == -1) {
            addOrdering(field, SortDirection.ASCENDING);
        } else {
            if (sortOrderings.get(index).getSortDirection() == SortDirection.ASCENDING) {
                sortOrderings.set(index, new Ordering(field, SortDirection.DESCENDING));
            } else {
                sortOrderings.set(index, new Ordering(field, SortDirection.ASCENDING));
            }
        }
        return this;
    }

    /**
     * Remove the sort ordering.
     *
     * @param field The field name.
     * @return Always returns the {@link PagingBuilder} object.
     */
    public PagingBuilder removeOrdering(final String field) {
        Iterables.removeIf(sortOrderings, findOrdering(field));
        return this;
    }

    /**
     * Construct the immutable {@link Paging} object from the builder configuration.
     *
     * @return The {@link Paging} object.
     */
    public Paging build() {
        return new Paging(pageNumber, pageSize, sortOrderings.toArray(new Ordering[0]));
    }
}
