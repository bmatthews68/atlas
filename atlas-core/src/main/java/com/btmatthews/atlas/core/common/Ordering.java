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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * An individual sort ordering.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class Ordering {
    /**
     * The field name.
     */
    private String sortField;
    /**
     * The sort direction.
     */
    private SortDirection sortDirection;

    /**
     * Initialize the ordering object.
     *
     * @param field     The name of the sort field.
     * @param direction The sort direction.
     */
    public Ordering(final String field, final SortDirection direction) {
        sortField = field;
        sortDirection = direction;
    }

    /**
     * Get the name of the sort field.
     *
     * @return The field name.
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * Get the sort direction.
     *
     * @return The direction.
     */
    public SortDirection getSortDirection() {
        return sortDirection;
    }

    /**
     * Compare two sort ordering.
     *
     * @param obj The other sort ordering.
     * @return {@code true} if both sort ordering are equal. Otherwise, {@code false}.
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Calculate a hash code for this sort ordering.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(sortField)
                .append(sortDirection.ordinal())
                .toHashCode();
    }

    /**
     * Convert the sort ordering to a string.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("sortField", sortField)
                .append("sortDirection", sortDirection)
                .toString();
    }
}
