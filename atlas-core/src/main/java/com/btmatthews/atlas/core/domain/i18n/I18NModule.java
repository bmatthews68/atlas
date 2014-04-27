package com.btmatthews.atlas.core.domain.i18n;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class I18NModule extends SimpleModule {

    public I18NModule() {
        super("I18NModule", new Version(1, 0, 0, null));
        addDeserializer(Localized.class, new LocalizedDeserializer());
        addSerializer(Localized.class, new LocalizedSerializer());
    }
}
