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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Unit test the {@link Ordering} object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestOrdering {

    private static final Ordering ASC_ORDERING = new Ordering("name", SortDirection.ASCENDING);
    private static final Ordering DESC_ORDERING = new Ordering("name", SortDirection.DESCENDING);
    private static final Ordering ASC_ORDERING_OTHER = new Ordering("name", SortDirection.ASCENDING);
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void verifyGetters() {
        collector.checkThat(ASC_ORDERING.getSortField(), is(equalTo("name")));
        collector.checkThat(ASC_ORDERING.getSortDirection(), is(equalTo(SortDirection.ASCENDING)));
        collector.checkThat(DESC_ORDERING.getSortField(), is(equalTo("name")));
        collector.checkThat(DESC_ORDERING.getSortDirection(), is(equalTo(SortDirection.DESCENDING)));
    }

    @Test
    public void verifyEquals() {
        collector.checkThat(ASC_ORDERING.equals(ASC_ORDERING), is(true));
        collector.checkThat(ASC_ORDERING.equals(DESC_ORDERING), is(false));
        collector.checkThat(ASC_ORDERING.equals(null), is(false));
        collector.checkThat(ASC_ORDERING.equals(ASC_ORDERING_OTHER), is(true));
    }

    @Test
    public void verifyHashCode() {
        collector.checkThat(ASC_ORDERING.hashCode(), is(124850432));
        collector.checkThat(DESC_ORDERING.hashCode(), is(124850433));
        collector.checkThat(ASC_ORDERING_OTHER.hashCode(), is(124850432));
    }

    @Test
    public void verifyToString() {
        collector.checkThat(ASC_ORDERING.toString(), is(equalTo("Ordering[sortField=name,sortDirection=ASCENDING]")));
        collector.checkThat(DESC_ORDERING.toString(), is(equalTo("Ordering[sortField=name,sortDirection=DESCENDING]")));
        collector.checkThat(ASC_ORDERING_OTHER.toString(), is(equalTo("Ordering[sortField=name,sortDirection=ASCENDING]")));
    }
}
