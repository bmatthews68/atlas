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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;
import org.mongojack.MongoCollection;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implements a person object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class PersonImpl implements Person {

    /**
     * The object identifier.
     */
    @Id
    private String id;
    /**
     * The person's name.
     */
    private String name;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    /**
     * Construct a person object.
     *
     * @param id   The object identifier.
     * @param name The person's name.
     */
    @JsonCreator
    public PersonImpl(@Id @JsonProperty("id") final String id,
                      @JsonProperty("name") final String name,
                      @JsonProperty("validFrom") final LocalDateTime validFrom,
                      @JsonProperty("validTo") final LocalDateTime validTo) {
        this.id = id;
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    /**
     * Get the object identifier.
     *
     * @return The object identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the person's name.
     *
     * @return The person's name.
     */
    public String getName() {
        return name;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }
}
