package com.btmatthews.atlas.core.dao.mongo;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;

public class MongoJSR310Module extends SimpleModule {

    public MongoJSR310Module() {
        super("MongoJSR310Module", new Version(1, 0, 0, null));
        addDeserializer(LocalDateTime.class, new MongoLocalDateTimeDeserializer());
        addSerializer(LocalDateTime.class, new MongoLocalDateTimeSerializer());
    }
}
