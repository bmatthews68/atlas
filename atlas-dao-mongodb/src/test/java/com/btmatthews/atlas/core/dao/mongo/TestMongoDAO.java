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
import com.btmatthews.atlas.core.common.PagingBuilder;
import com.btmatthews.atlas.core.dao.DAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.Currency;

import static org.hamcrest.Matchers.*;

/**
 * Unit test the {@link MongoDAO} data access object implementation.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestMongoDAO {

    /**
     * Used to collect test failures within a single test case.
     */
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    /**
     * The data access object being tested.
     */
    private DAO<String, Person> dao;
    /**
     * Used to mock the Mongo data store.
     */
    private Fongo fongo = new Fongo("localhost");

    /**
     * Prepare for test case execution.
     */
    @Before
    public void setup() {
        dao = new MongoDAO<String,Person,PersonImpl>(fongo.getMongo(), new ObjectMapper(), PersonImpl.class, "db", "people");
    }

    /**
     * Make sure the {@link MongoDAO#find(com.btmatthews.atlas.core.common.Paging)} throws an {@link IllegalArgumentException} if {@code null} is
     * passed as the {@code paging} parameter.
     *
     * @throws Exception An {@link IllegalArgumentException} is expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void findWithNullPagingShouldFail() throws Exception {
        dao.find(null);
    }

    /**
     * Test the {@link com.btmatthews.atlas.core.dao.mongo.MongoDAO#count()},
     * {@link com.btmatthews.atlas.core.dao.mongo.MongoDAO#find(com.btmatthews.atlas.core.common.Paging)},
     * {@link com.btmatthews.atlas.core.dao.mongo.MongoDAO#create(Object, Object)},
     * {@link com.btmatthews.atlas.core.dao.mongo.MongoDAO#read(Object)},
     * {@link com.btmatthews.atlas.core.dao.mongo.MongoDAO#update(Object, Object)} and
     * {@link com.btmatthews.atlas.core.dao.mongo.MongoDAO#destroy(Object)} methods.
     */
    @Test
    public void checkFullObjectLifecycle() {
       /* final Paging paging = new PagingBuilder().setPageNumber(0).setPageSize(100).build();
        collector.checkThat(dao.count(), is(equalTo(0L)));
        collector.checkThat(dao.find(paging).size(), is(equalTo(0)));
        final Person person1 = new PersonImpl("ee749160-c6a0-11e2-8b8b-0800200c9a66", "Brian Matthews");
        dao.create("ee749160-c6a0-11e2-8b8b-0800200c9a66", person1);
        collector.checkThat(dao.count(), is(equalTo(1L)));
        collector.checkThat(dao.find(paging).size(), is(equalTo(1)));
        final Person person2 = dao.read("ee749160-c6a0-11e2-8b8b-0800200c9a66");
        collector.checkThat(person2, is(not(nullValue())));
        collector.checkThat(person2, hasProperty("id", is(equalTo("ee749160-c6a0-11e2-8b8b-0800200c9a66"))));
        collector.checkThat(person2, hasProperty("name", is(equalTo("Brian Matthews"))));
        collector.checkThat(dao.count(), is(equalTo(1L)));
        final Person person3 = new PersonImpl("ee749160-c6a0-11e2-8b8b-0800200c9a66", "Brian Thomas Matthews");
        dao.update("ee749160-c6a0-11e2-8b8b-0800200c9a66", person3);
        final Person person4 = dao.read("ee749160-c6a0-11e2-8b8b-0800200c9a66");
        collector.checkThat(person4, is(not(nullValue())));
        collector.checkThat(person4, hasProperty("id", is(equalTo("ee749160-c6a0-11e2-8b8b-0800200c9a66"))));
        collector.checkThat(person4, hasProperty("name", is(equalTo("Brian Thomas Matthews"))));
        dao.destroy("ee749160-c6a0-11e2-8b8b-0800200c9a66");
        collector.checkThat(dao.count(), is(equalTo(0L)));
        collector.checkThat(dao.find(paging).size(), is(equalTo(0))); */
    }

    /**
     * Make sure the {@link MongoDAO#create(Object, Object)} method throws an {@link IllegalArgumentException} if {@code null} is
     * passed as the {@code entity} parameter.
     *
     * @throws Exception An {@link IllegalArgumentException} is expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createNullShouldFail() throws Exception {
        dao.create(null, null);
    }

    /**
     * Make sure the {@link MongoDAO#update(Object, Object)} method throws an {@link IllegalArgumentException} if {@code null} is
     * passed as the {@code entity} parameter.
     *
     * @throws Exception An {@link IllegalArgumentException} is expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateNullShouldFail() throws Exception {
        dao.update(null, null);
    }

    /**
     * Make sure the {@link MongoDAO#destroy(Object)} method throws an {@link IllegalArgumentException} if {@code null} is
     * passed as the {@code entity} parameter.
     *
     * @throws Exception An {@link IllegalArgumentException} is expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteNullShouldFail() throws Exception {
        dao.destroy(null);
    }

    /**
     * Make sure the {@link MongoDAO#read(Object)} method returns {@code null} if the object does not already exist
     * in the data store.
     */
    @Test
    public void readFromEmptyDatabaseShouldFail() {
        final Person result = dao.read("2c6c4910-c69f-11e2-8b8b-0800200c9a66");
        collector.checkThat(result, is(nullValue()));
    }
}
