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

package com.btmatthews.atlas.core.domain.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Localized<T> {

	private Map<Locale, T> values;

	public Localized(final Map<Locale, T> vals) {
		final ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
		values = builder.putAll(vals).build();
	}

	public T getValue(final Locale locale) {
		return values.get(locale);
	}

	public void setValue(final Locale locale, final T value) {
		values.put(locale, value);
	}

	public void removeValue(final Locale locale) {
		values.remove(locale);
	}

	public static <T> Localized<T> create(final Locale locale, final T val) {
		final Map<Locale, T> vals = new HashMap<Locale, T>();
		vals.put(locale, val);
		return new Localized<>(vals);
	}
}
