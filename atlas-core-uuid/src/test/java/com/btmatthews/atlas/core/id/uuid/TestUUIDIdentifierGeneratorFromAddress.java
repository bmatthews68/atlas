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

package com.btmatthews.atlas.core.id.uuid;

import com.btmatthews.atlas.core.id.IdentifierGenerator;
import org.junit.Before;
import org.junit.Test;

import static com.btmatthews.hamcrest.regex.PatternMatcher.matches;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class TestUUIDIdentifierGeneratorFromAddress {

    private IdentifierGenerator<String> generator;

    @Before
    public void setup() {
        generator = new UUIDIdentifierGenerator("df:bc:b7:3f:91:17");
    }

    @Test
    public void generateReturnsAUUIDString() {
        final String uuid = generator.generate();
        assertThat(uuid, is(notNullValue()));
        assertThat(uuid, matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }
}
