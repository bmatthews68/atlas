package com.btmatthews.atlas.core.dao.mongo;

import com.btmatthews.atlas.core.domain.i18n.Localized;
import com.btmatthews.atlas.core.domain.i18n.LocalizedBuilder;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class TestDBObjectEncoder {

    private DBObjectEncoder encoder;

    @Before
    public void setup() {
        encoder = new DBObjectEncoder();
    }

    @Test
    public void canEncodeLocalized() {
        final Localized<String> object = new LocalizedBuilder<String>()
                .setValue(Locale.ENGLISH, "Hello")
                .setValue(Locale.FRENCH, "Bonjour")
                .build();
        final DBObject dbObject = encoder.encode(object);
        assertNotNull(dbObject);
    }
}
