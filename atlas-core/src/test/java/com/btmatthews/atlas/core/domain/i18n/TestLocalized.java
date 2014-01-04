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
 * Unit test the {@link Localized} object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestLocalized {

    private static final String HELLO_WORLD_ENGLISH = "Hello World!";
    private static final String HELLO_WORLD_GERMAN = "Hallo Wert!";
    private static final String GOODBYE_WORLD_ENGLISH = "Goodbye World!";
    private static final String GOODBYE_WORLD_GERMAN = "Auf Wiedersehen, Welt!";
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private Localized<String> single;
    private Localized<String> single_copy;
    private Localized<String> single_different;
    private Localized<String> multi;
    private Localized<String> multi_copy;
    private Localized<String> multi_different;

    @Before
    public void setup() {
        single = new Localized<String>(Locale.ENGLISH, HELLO_WORLD_ENGLISH);
        single_copy = new Localized<String>(Locale.ENGLISH, HELLO_WORLD_ENGLISH);
        single_different = new Localized<String>(Locale.ENGLISH, GOODBYE_WORLD_ENGLISH);
        Map<Locale, String> values = new HashMap<Locale, String>();
        values.put(Locale.ENGLISH, HELLO_WORLD_ENGLISH);
        values.put(Locale.GERMAN, HELLO_WORLD_GERMAN);
        multi = new Localized<String>(values);
        multi_copy = new Localized<String>(values);
        values = new HashMap<Locale, String>();
        values.put(Locale.ENGLISH, GOODBYE_WORLD_ENGLISH);
        values.put(Locale.GERMAN, GOODBYE_WORLD_GERMAN);
        multi_different = new Localized<String>(values);
    }

    @Test
    public void createSingleLocalizedValue() {
        collector.checkThat(single.getValue(Locale.ENGLISH), is(equalTo(HELLO_WORLD_ENGLISH)));
        collector.checkThat(single.getValues().size(), is(equalTo(1)));
        collector.checkThat(single.getValues().get(Locale.ENGLISH), is(equalTo(HELLO_WORLD_ENGLISH)));
        collector.checkThat(single.hashCode(), is(equalTo(-1006767329)));
        collector.checkThat(single_copy.hashCode(), is(equalTo(-1006767329)));
        collector.checkThat(single_different.hashCode(), is(equalTo(1598763198)));
        collector.checkThat(single, is(equalTo(single)));
        collector.checkThat(single, is(equalTo(single_copy)));
        collector.checkThat(single, is(not(equalTo(null))));
        collector.checkThat(single, is(not(equalTo(single_different))));
        collector.checkThat(single.toString(), is(equalTo("Localized[{en=" + HELLO_WORLD_ENGLISH + "}]")));
        collector.checkThat(single_copy.toString(), is(equalTo("Localized[{en=" + HELLO_WORLD_ENGLISH + "}]")));
        collector.checkThat(single_different.toString(), is(equalTo("Localized[{en=" + GOODBYE_WORLD_ENGLISH + "}]")));
    }

    @Test
    public void createWithMultipleLocalizedValues() {
        collector.checkThat(multi.getValue(Locale.ENGLISH), is(equalTo(HELLO_WORLD_ENGLISH)));
        collector.checkThat(multi.getValue(Locale.GERMAN), is(equalTo(HELLO_WORLD_GERMAN)));
        collector.checkThat(multi.getValues().size(), is(equalTo(2)));
        collector.checkThat(multi.getValues().get(Locale.ENGLISH), is(equalTo(HELLO_WORLD_ENGLISH)));
        collector.checkThat(multi.getValues().get(Locale.GERMAN), is(equalTo(HELLO_WORLD_GERMAN)));
        collector.checkThat(multi.hashCode(), is(equalTo(-1027518473)));
        collector.checkThat(multi_copy.hashCode(), is(equalTo(-1027518473)));
        collector.checkThat(multi_different.hashCode(), is(equalTo(2008373625)));
        collector.checkThat(multi, is(equalTo(multi)));
        collector.checkThat(multi, is(equalTo(multi_copy)));
        collector.checkThat(multi, is(not(equalTo(null))));
        collector.checkThat(multi, is(not(equalTo(multi_different))));
        collector.checkThat(multi.toString(), is(equalTo("Localized[{de=" + HELLO_WORLD_GERMAN + ", " +
                "en=" + HELLO_WORLD_ENGLISH + "}]")));
        collector.checkThat(multi_copy.toString(), is(equalTo("Localized[{de=" + HELLO_WORLD_GERMAN + ", " +
                "en=" + HELLO_WORLD_ENGLISH + "}]")));
        collector.checkThat(multi_different.toString(), is(equalTo("Localized[{de=" + GOODBYE_WORLD_GERMAN + ", " +
                "en=" + GOODBYE_WORLD_ENGLISH + "}]")));
    }
}
