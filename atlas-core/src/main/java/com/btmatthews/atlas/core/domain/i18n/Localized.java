/*
 * Copyright 2011-2013 Brian Matthews
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A localized value. Maps Java locales to the locale-specific value.
 *
 * @param <T> The value type.
 */
public class Localized<T> {

    /**
     * Maps the Java locale to the locale-specific value.
     */
    private ImmutableMap<Locale, T> values;

    private Localized(final Locale locale, T val) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.put(locale, val).build();
    }

    /**
     * Construct a localized value with the values.
     * @param vals
     */
    public Localized(final Map<Locale, T> vals) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.putAll(vals).build();
    }

    /**
     * Create a singleton localized value.
     *
     * @param locale The locale.
     * @param val    The value.
     * @param <T>    The value type.
     * @return The singledton localized value.
     */
    public static <T> Localized<T> create(final Locale locale, final T val) {
        return new Localized(locale, val);
    }

    /**
     * Get a locale specific value.
     * @param locale              The locale.
     * @return
     */
    public T getValue(final Locale locale) {
        return values.get(locale);
    }

    public void setValue(final Locale locale, final T value) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        values = builder.putAll(values).put(locale, value).build();
    }

    public void removeValue(final Locale locale) {
        final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        builder.putAll(values);
        values.remove(locale);
    }
}
