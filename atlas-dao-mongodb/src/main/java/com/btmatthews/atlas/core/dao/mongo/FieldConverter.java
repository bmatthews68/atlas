package com.btmatthews.atlas.core.dao.mongo;

import java.lang.reflect.Field;

public interface FieldConverter {

    Object encode(Object source, Field field) throws IllegalAccessException;

    void decode(Object target, Object value, Field field, DBObjectDecoder decoder) throws IllegalAccessException;

    boolean canDecode(Object value, Field field);

    boolean canEncode(Object value, Field field);
}
