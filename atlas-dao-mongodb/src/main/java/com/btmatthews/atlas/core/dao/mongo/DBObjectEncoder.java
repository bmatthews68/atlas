package com.btmatthews.atlas.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Created by bmatthews68 on 25/03/2014.
 */
public class DBObjectEncoder {

    public DBObject encode(final Object object) {
        try {
            return encode(object, null);
        } catch (final IllegalAccessException e) {
            return null;
        }
    }

    private DBObject encode(final Object object,
                            final DBObject parent)
            throws IllegalAccessException {
        final BasicDBObject dbObject = new BasicDBObject();
        final Field[] fields = object.getClass().getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            final String fieldName = getName(field, parent);
            final Object fieldValue;
            if (String.class.equals(field.getType())) {
                fieldValue = field.get(object);
            } else if (int.class.equals(field.getType())) {
                fieldValue = Integer.valueOf(field.getInt(object));
            } else if (LocalDateTime.class.equals(field.getType())) {
                final LocalDateTime value = (LocalDateTime) field.get(object);
                if (value == null) {
                    fieldValue = null;
                } else {
                    fieldValue = Date.from(value.toInstant(ZoneOffset.UTC));
                }
            } else {
                final Object value = field.get(object);
                if (value == null) {
                    fieldValue = null;
                } else {
                    fieldValue = encode(field.get(object), dbObject);
                }
            }
            if (fieldValue != null) {
                dbObject.put(fieldName, fieldValue);
            }
        }
        return dbObject;
    }

    private String getName(final Field field,
                           final DBObject parent) {
        final String fieldName = field.getName();
        if (fieldName.equals("id") && parent == null) {
            return "_id";
        } else {
            return fieldName;
        }
    }
}
