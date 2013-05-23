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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A builder for {@link Localized} objects that map Java locales to the locale-specific value.
 *
 * @param <T> The value type.
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class LocalizedBuilder<T> {

    /**
     * Maps the Java locale to the locale-specific value.
     */
    private Map<Locale, T> values = new HashMap<Locale, T>();

    /**
     * Construct a builder that will be used to create a new {@link Localized} object from scratch.
     */
    public LocalizedBuilder() {
    }

    /**
     * Construct a builder that will be used to modify an existing {@link Localized} object.
     *
     * @param original The original {@link Localized} object.
     */
    public LocalizedBuilder(final Localized<T> original) {
        values.putAll(original.getValues());
    }

    /**
     * Set a locale specific value.
     *
     * @param locale The locale.
     * @param value  The local specific value.
     * @returns The builder object.
     */
    public LocalizedBuilder<T> setValue(final Locale locale, final T value) {
        values.put(locale, value);
        return this;
    }

    /**
     * Remove a locale specific value.
     *
     * @param locale The locale.
     * @returns The builder object.
     */
    public LocalizedBuilder<T> removeValue(final Locale locale) {
        values.remove(locale);
        return this;
    }

    /**
     * Construct an immutable {@link Localized} object.
     *
     * @return The {@link Localized} object.
     */
    public Localized<T> build() {
        return new Localized<T>(values);
    }
}
