package com.btmatthews.atlas.core.dao.mongo;

import com.mongodb.DBObject;

import java.lang.reflect.Field;

public class NameUtils {

    public static final String getName(final Field field,
                                       final DBObject parent) {
        final String fieldName = field.getName();
        if (fieldName.equals("id") && parent == null) {
            return "_id";
        } else {
            return fieldName;
        }
    }
}
