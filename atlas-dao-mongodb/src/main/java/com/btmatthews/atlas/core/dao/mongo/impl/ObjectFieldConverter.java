package com.btmatthews.atlas.core.dao.mongo.impl;

import com.btmatthews.atlas.core.dao.mongo.DBObjectDecoder;
import com.btmatthews.atlas.core.dao.mongo.FieldConverter;

import java.lang.reflect.Field;

public class ObjectFieldConverter implements FieldConverter {
    @Override
    public Object encode(final Object source,
                         final Field field)
            throws IllegalAccessException {
        return null;
    }

    @Override
    public void decode(final Object target,
                       final Object value,
                       final Field field,
                       final DBObjectDecoder decoder)
            throws IllegalAccessException {

        if (value instanceof String) {
            field.set(target, value);
        } else {
            field.set(target, value.toString());
        }
    }

    @Override
    public boolean canDecode(final Object value,
                             final Field field) {
        return value != null && String.class.equals(field.getType());
    }

    @Override
    public boolean canEncode(final Object value,
                             final Field field) {
        return value != null && String.class.equals(field.getType());
    }
}
