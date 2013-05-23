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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test the {@link Paging} object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestPaging {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void verifyBasicConstructor() {
        final Paging paging = new Paging(2, 100);
        collector.checkThat(paging.getPageNumber(), is(equalTo(2)));
        collector.checkThat(paging.getPageSize(), is(equalTo(100)));
        collector.checkThat(paging.hashCode(), is(equalTo(867540)));
        collector.checkThat(paging, is(equalTo(paging)));
        collector.checkThat(paging, is(equalTo(new Paging(2, 100))));
        collector.checkThat(paging, is(not(equalTo(new Paging(0, 100)))));
        collector.checkThat(paging, is(not(equalTo(null))));
        collector.checkThat(paging.toString(), is(equalTo("Paging[pageNumber=2,pageSize=100,sortOrderings=[]]")));
    }

    @Test
    public void verifyConstructorWithSingleSort() {
        final Paging paging = new Paging(2, 100, new Ordering("name", SortDirection.ASCENDING));
        collector.checkThat(paging.getPageNumber(), is(equalTo(2)));
        collector.checkThat(paging.getPageSize(), is(equalTo(100)));
        collector.checkThat(paging.hashCode(), is(equalTo(125718002)));
        collector.checkThat(paging, is(equalTo(paging)));
        collector.checkThat(paging, is(equalTo(new Paging(2, 100, new Ordering("name", SortDirection.ASCENDING)))));
        collector.checkThat(paging, is(not(equalTo(new Paging(2, 100)))));
        collector.checkThat(paging, is(not(equalTo(new Paging(0, 100, new Ordering("name", SortDirection.ASCENDING))))));
        collector.checkThat(paging, is(not(equalTo(new Paging(2, 100, new Ordering("name", SortDirection.DESCENDING))))));
        collector.checkThat(paging, is(not(equalTo(null))));
        collector.checkThat(paging.toString(), is(equalTo("Paging[pageNumber=2,pageSize=100,sortOrderings=[Ordering[sortField=name,sortDirection=ASCENDING]]]")));
    }
}
