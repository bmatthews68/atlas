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

package com.btmatthews.atlas.core.dao;

import java.util.List;

import com.btmatthews.atlas.core.common.Paging;

/**
 * Describes the interface for data access objects that persist entities of
 * described by the interface {@code I} to a data store.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * 
 * @param <I>
 *            The interface that describes the persistent entity.
 */
public interface DAO<I> {

	long count();
	
	List<I> find(Paging paging);
	
	/**
	 * Persist a newly created entity in the data store.
	 * 
	 * @param entity
	 *            The newly created entity.
	 */
	void create(I entity);

	/**
	 * Retrieve a persistent entity from the data store.
	 * 
	 * @param id
	 *            The object identifier of the persistent entity.
	 * @return The persistent entity.
	 */
	I read(String id);

	/**
	 * Update a persistent entity in the data store.
	 * 
	 * @param entity
	 *            The persistent entity.
	 */
	void update(I entity);

	/**
	 * Delete a persistent entity from the data store.
	 * 
	 * @param entity
	 *            The persistent entity.
	 */
	void destroy(I entity);
}
