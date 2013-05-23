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

package com.btmatthews.atlas.core.domain.i18n;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Maps.filterKeys;

/**
 * A localized value. Maps Java locales to the locale-specific value.
 *
 * @param <T> The value type.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class Localized<T> {

    /**
     * Maps the Java locale to the locale-specific value.
     */
    private ImmutableMap<Locale, T> values;

    /**
     * Construct a localized value with a single entry.
     *
     * @param locale The locale.
     * @param val    The value.
     */
    private Localized(final Locale locale, T val) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.put(locale, val).build();
    }

    /**
     * Construct a localized value with the values.
     *
     * @param vals Maps locales to values.
     */
    private Localized(final Map<Locale, T> vals) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.putAll(vals).build();
    }

    /**
     * Create a singleton localized value.
     *
     * @param locale The locale.
     * @param val    The value.
     * @param <T>    The value type.
     * @return The singleton localized value.
     */
    public static <T> Localized<T> create(final Locale locale, final T val) {
        return new Localized(locale, val);
    }

    /**
     * Create a localized value.
     *
     * @param vals Maps locales to values.
     * @param <T>  The value type.
     * @return The localized value.
     */
    public static <T> Localized<T> create(final Map<Locale, T> vals) {
        return new Localized<T>(vals);
    }

    /**
     * Get a locale specific value.
     *
     * @param locale The locale.
     * @return The locale specific value.
     */
    public T getValue(final Locale locale) {
        return values.get(locale);
    }

    /**
     * Set a locale specific value.
     *
     * @param locale The locale.
     * @param value  The local specific value.
     */
    public void setValue(final Locale locale, final T value) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.putAll(values).put(locale, value).build();
    }

    /**
     * Remove a locale specific value.
     *
     * @param locale The locale.
     */
    public void removeValue(final Locale locale) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.putAll(filterKeys(values, not(equalTo(locale)))).build();
    }

    /**
     * Compare two localized values.
     *
     * @param obj The other localized values.
     * @return {@code true} if both localized values are equal. Otherwise, {@code false}.
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Calculate a hash code for this localized value.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(values).toHashCode();
    }

    /**
     * Convert the localized value to a string.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append(values).toString();
    }
}
