package com.btmatthews.atlas.core.dao.mongo.impl;

import com.btmatthews.atlas.core.dao.mongo.DBObjectDecoder;
import com.btmatthews.atlas.core.dao.mongo.FieldConverter;

import java.lang.reflect.Field;

/**
 * Created by bmatthews68 on 26/03/2014.
 */
public class IntFieldConverter implements FieldConverter {
    @Override
    public Object encode(final Object source,
                         final Field field)
            throws IllegalAccessException {
        return Integer.valueOf((Integer) field.get(source));
    }

    @Override
    public void decode(final Object target,
                       final Object value,
                       final Field field,
                       final DBObjectDecoder decoder)
            throws IllegalAccessException {
        field.setInt(target, (Integer) value);
    }

    @Override
    public boolean canDecode(final Object value,
                             final Field field) {
        return value != null && int.class.equals(field.getType());
    }

    @Override
    public boolean canEncode(final Object value,
                             final Field field) {
        return value != null && int.class.equals(field.getType());
    }
}
