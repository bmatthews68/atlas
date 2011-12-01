/*
 * Copyright 2011 Brian Matthews
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

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public final class Paging {

	public enum SortDirection {
		ASCENDING, DESCENDING
	}

	public final class Ordering {
		private String sortField;
		private SortDirection sortDirection;

		public Ordering(final String field, final SortDirection direction) {
			sortField = field;
			sortDirection = direction;
		}

		public String getSortField() {
			return sortField;
		}

		public SortDirection getSortDirection() {
			return sortDirection;
		}
	}

	private int pageNumber;

	private int pageSize;

	private List<Ordering> sortOrderings;

	public Paging(final int number, final int size, final Ordering... sorts) {
		pageNumber = number;
		pageSize = size;
		sortOrderings = Lists.newArrayList(sorts);
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<Ordering> getSortOrderings() {
		return sortOrderings;
	}

	public void addSortOrdering(final String field,
			final SortDirection direction) {
		final Ordering ordering = new Ordering(field, direction);
		sortOrderings.add(ordering);
	}

	public void toggleSortOdering(final String field) {
		final Ordering ordering = Iterables.find(sortOrderings,
				new Predicate<Ordering>() {
					public boolean apply(final Ordering ordering) {
						return ordering.getSortField().equals(field);
					};
				});
		if (ordering == null) {
			addSortOrdering(field, SortDirection.ASCENDING);
		} else {
			if (ordering.getSortDirection() == SortDirection.ASCENDING) {
				removeSortOrdering(field);
				addSortOrdering(field, SortDirection.DESCENDING);
			} else {
				removeSortOrdering(field);
				addSortOrdering(field, SortDirection.ASCENDING);
			}
		}
	}

	public void removeSortOrdering(final String field) {
		Iterables.removeIf(sortOrderings, new Predicate<Ordering>() {
			public boolean apply(final Ordering ordering) {
				return ordering.getSortField().equals(field);
			};
		});
	}

}
