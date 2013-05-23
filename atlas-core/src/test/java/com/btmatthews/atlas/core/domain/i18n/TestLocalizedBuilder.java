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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

/**
 * Unit test the {@link LocalizedBuilder} object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestLocalizedBuilder {

    private static final String HELLO_WORLD_ENGLISH = "Hello World!";
    private static final String HELLO_WORLD_GERMAN = "Hallo Wert!";
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private Localized<String> original;
    private Map<Locale, String> values;

    @Before
    public void setup() {
        values = new HashMap<Locale, String>();
        values.put(Locale.ENGLISH, HELLO_WORLD_ENGLISH);
        values.put(Locale.GERMAN, HELLO_WORLD_GERMAN);
        original = new Localized<String>(values);
    }

    @Test
    public void createEmpty() {
        final Localized<String> localized = new LocalizedBuilder<String>().build();
        collector.checkThat(localized, is(not(nullValue())));
        collector.checkThat(localized.getValues(), is(not(nullValue())));
        collector.checkThat(localized.getValues().size(), is(equalTo(0)));
    }

    @Test
    public void createSingle() {
        final Localized<String> localized = new LocalizedBuilder<String>()
                .setValue(Locale.ENGLISH, HELLO_WORLD_ENGLISH)
                .build();
        collector.checkThat(localized, is(not(nullValue())));
        collector.checkThat(localized.getValue(Locale.ENGLISH), is(equalTo("Hello World!")));
        collector.checkThat(localized.getValues(), is(not(nullValue())));
        collector.checkThat(localized.getValues().size(), is(equalTo(1)));
    }

    @Test
    public void copyOriginal() {
        final Localized<String> localized = new LocalizedBuilder<String>(original).build();
        collector.checkThat(localized, is(not(nullValue())));
        collector.checkThat(localized.getValues().size(), is(equalTo(2)));
        collector.checkThat(localized.getValue(Locale.ENGLISH), is(equalTo(HELLO_WORLD_ENGLISH)));
        collector.checkThat(localized.getValue(Locale.GERMAN), is(equalTo(HELLO_WORLD_GERMAN)));
    }

    @Test
    public void removeValue() {
        final Localized<String> localized = new LocalizedBuilder<String>(original).removeValue(Locale.ENGLISH).build();
        collector.checkThat(localized, is(not(nullValue())));
        collector.checkThat(localized.getValues().size(), is(equalTo(1)));
        collector.checkThat(localized.getValue(Locale.ENGLISH), is(nullValue()));
        collector.checkThat(localized.getValue(Locale.GERMAN), is(equalTo(HELLO_WORLD_GERMAN)));
    }
}
