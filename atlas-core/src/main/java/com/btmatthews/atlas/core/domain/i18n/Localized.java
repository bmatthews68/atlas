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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.common.collect.Ordering.natural;

/**
 * A localized value. Maps Java locales to the locale-specific value.
 *
 * @param <T> The value type.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
@JsonSerialize(using = LocalizedSerializer.class)
@JsonDeserialize(using = LocalizedDeserializer.class)
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
    public Localized(final Locale locale, T val) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.put(locale, val).build();
    }

    /**
     * Construct a localized value with the values.
     *
     * @param vals Maps locales to values.
     */
    public Localized(final Map<Locale, T> vals) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.putAll(vals).build();
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

    public ImmutableMap<Locale, T> getValues() {
        return values;
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
        final StringBuilder builder = new StringBuilder("Localized[{");
        boolean first = true;
        final List<Locale> keys = natural()
                .onResultOf(new Function<Locale, Comparable>() {
                    @Override
                    public Comparable apply(Locale o) {
                        return o.toLanguageTag();
                    }
                })
                .sortedCopy(values.keySet());
        for (final Locale locale : keys) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(locale.toLanguageTag());
            builder.append('=');
            builder.append(values.get(locale));
        }
        builder.append("}]");
        return builder.toString();
    }
}
