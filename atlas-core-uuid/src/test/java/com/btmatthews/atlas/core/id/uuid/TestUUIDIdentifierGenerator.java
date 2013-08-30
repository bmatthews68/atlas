package com.btmatthews.atlas.core.id.uuid;

import com.btmatthews.atlas.core.id.IdentifierGenerator;
import com.btmatthews.hamcrest.regex.PatternMatcher;
import org.junit.Before;
import org.junit.Test;

import static com.btmatthews.hamcrest.regex.PatternMatcher.matches;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by bmatthews68 on 20/05/2013.
 */
public class TestUUIDIdentifierGenerator {

    private IdentifierGenerator<String> generator;

    @Before
    public void setup() {
        generator = new UUIDIdentifierGenerator();
    }

    @Test
    public void generateReturnsAUUIDString() {
        final String uuid = generator.generate();
        assertThat(uuid, is(notNullValue()));
        assertThat(uuid, matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }
}
