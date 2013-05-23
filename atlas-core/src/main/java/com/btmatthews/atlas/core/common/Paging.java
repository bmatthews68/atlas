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
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * A paging object describes the page number, page size and sort ordering to use when returning
 * large result sets.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class Paging {

    /**
     * The page number.
     */
    private int pageNumber;
    /**
     * The page size.
     */
    private int pageSize;
    /**
     * The sort fields and directions.
     */
    private List<Ordering> sortOrderings;

    /**
     * Initialise the paging object.
     *
     * @param number The page number.
     * @param size   The page size.
     * @param sorts  The sort orderings.
     */
    public Paging(final int number, final int size, final Ordering... sorts) {
        pageNumber = number;
        pageSize = size;
        sortOrderings = Lists.newArrayList(sorts);
    }

    /**
     * Get the page number.
     *
     * @return The page number.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Get the page size.
     *
     * @return The page size.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Get the sort orderings.
     *
     * @return The sort orderings.
     */
    public List<Ordering> getSortOrderings() {
        return sortOrderings;
    }

    /**
     * Compare two paging objects.
     *
     * @param obj The other paging object.
     * @return {@code true} if both paging object are equal. Otherwise, {@code false}.
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Calculate a hash code for this paging object.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(pageNumber)
                .append(pageSize)
                .append(sortOrderings)
                .toHashCode();
    }

    /**
     * Convert the paging object to a string.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("pageNumber", pageNumber)
                .append("pageSize", pageSize)
                .append("sortOrderings", sortOrderings)
                .toString();
    }
}
