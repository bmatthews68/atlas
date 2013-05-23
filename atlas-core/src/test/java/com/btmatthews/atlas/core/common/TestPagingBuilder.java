package com.btmatthews.atlas.core.common;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by bmatthews68 on 22/05/2013.
 */
public class TestPagingBuilder {

    private Paging original = new Paging(0, 100, new Ordering("class", SortDirection.ASCENDING), new Ordering("name", SortDirection.ASCENDING), new Ordering("score", SortDirection.DESCENDING));

    @Test
    public void hasSensibleDefaults() {
        final Paging paging = new PagingBuilder().build();
        assertThat(paging.getPageNumber(), is(equalTo(0)));
        assertThat(paging.getPageSize(), is(equalTo(1)));
        assertThat(paging.getSortOrderings(), is(notNullValue()));
        assertThat(paging.getSortOrderings().size(), is(equalTo(0)));
    }

    @Test
    public void canOverridePageNumber() {
        final Paging paging = new PagingBuilder().setPageNumber(2).build();
        assertThat(paging.getPageNumber(), is(equalTo(2)));
        assertThat(paging.getPageSize(), is(equalTo(1)));
        assertThat(paging.getSortOrderings(), is(notNullValue()));
        assertThat(paging.getSortOrderings().size(), is(equalTo(0)));
    }

    @Test
    public void canOverridePageSize() {
        final Paging paging = new PagingBuilder().setPageSize(100).build();
        assertThat(paging.getPageNumber(), is(equalTo(0)));
        assertThat(paging.getPageSize(), is(equalTo(100)));
        assertThat(paging.getSortOrderings(), is(notNullValue()));
        assertThat(paging.getSortOrderings().size(), is(equalTo(0)));
    }

    @Test
    public void canAddSortOrdering() {
        final Paging paging = new PagingBuilder().addOrdering("name", SortDirection.ASCENDING).build();
        assertThat(paging.getPageNumber(), is(equalTo(0)));
        assertThat(paging.getPageSize(), is(equalTo(1)));
        assertThat(paging.getSortOrderings(), is(notNullValue()));
        assertThat(paging.getSortOrderings().size(), is(equalTo(1)));
        assertThat(paging.getSortOrderings(), hasItem(equalTo(new Ordering("name", SortDirection.ASCENDING))));
    }

    @Test
    public void canAddMultipleSortOrderings() {
        final Paging paging = new PagingBuilder().addOrdering("name", SortDirection.ASCENDING).addOrdering("score", SortDirection.DESCENDING).build();
        assertThat(paging.getPageNumber(), is(equalTo(0)));
        assertThat(paging.getPageSize(), is(equalTo(1)));
        assertThat(paging.getSortOrderings(), is(notNullValue()));
        assertThat(paging.getSortOrderings().size(), is(equalTo(2)));
        assertThat(paging.getSortOrderings(), hasItems(equalTo(new Ordering("name", SortDirection.ASCENDING)), equalTo(new Ordering("score", SortDirection.DESCENDING))));
    }

    @Test
    public void checkPagingBuilderWithOverrides() {
        final Paging paging = new PagingBuilder().setPageNumber(2).setPageSize(100).addOrdering("name", SortDirection.ASCENDING).build();
        assertThat(paging.getPageNumber(), is(equalTo(2)));
        assertThat(paging.getPageSize(), is(equalTo(100)));
        assertThat(paging.getSortOrderings(), is(notNullValue()));
        assertThat(paging.getSortOrderings().size(), is(equalTo(1)));
        assertThat(paging.getSortOrderings(), hasItem(equalTo(new Ordering("name", SortDirection.ASCENDING))));
    }

    @Test
    public void copyExisting() {
        final Paging paging = new PagingBuilder(original).build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=name,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void changePageNumber() {
        final Paging paging = new PagingBuilder(original).setPageNumber(2).build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=2,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=name,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void changePageSize() {
        final Paging paging = new PagingBuilder(original).setPageSize(50).build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=50,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=name,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void promoteSortOrdering() {
        final Paging paging = new PagingBuilder(original).promoteOrdering("name").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=name,sortDirection=ASCENDING], Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void promoteFirstSortOrdering() {
        final Paging paging = new PagingBuilder(original).promoteOrdering("class").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=name,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void demoteSortOrdering() {
        final Paging paging = new PagingBuilder(original).demoteOrdering("name").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING], Ordering[sortField=name,sortDirection=ASCENDING]]]")));
    }

    @Test
    public void removeSortOrdering() {
        final Paging paging = new PagingBuilder(original).removeOrdering("name").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void toggleCreatesNewOrderingEntryIfRequired() {
        final Paging paging = new PagingBuilder().toggleOrdering("name").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=1,sortOrderings=[Ordering[sortField=name,sortDirection=ASCENDING]]]")));
    }

    @Test
    public void toggleSortOrderingToDescending() {
        final Paging paging = new PagingBuilder(original).toggleOrdering("name").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=name,sortDirection=DESCENDING], Ordering[sortField=score,sortDirection=DESCENDING]]]")));
    }

    @Test
    public void toggleSortOrderingToAscending() {
        final Paging paging = new PagingBuilder(original).toggleOrdering("score").build();
        assertThat(paging.toString(), is(equalTo("Paging[pageNumber=0,pageSize=100,sortOrderings=[Ordering[sortField=class,sortDirection=ASCENDING], Ordering[sortField=name,sortDirection=ASCENDING], Ordering[sortField=score,sortDirection=ASCENDING]]]")));
    }

}
