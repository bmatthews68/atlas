package com.btmatthews.atlas.core.domain.jsr310;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;

public class JSR310Module extends SimpleModule {

    public JSR310Module() {
        super("JSR310Module", new Version(1, 0, 0, null));
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    }
}
