package com.btmatthews.atlas.core.dao.mongo;

import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by bmatthews68 on 26/03/2014.
 */
public class DBObjectDecoder {

    private final List<FieldConverter> fieldConverters;

    public DBObjectDecoder(final List<FieldConverter> fieldConverters) {
        this.fieldConverters = fieldConverters;
    }

    public <I> I decode(final DBObject dbObject,
                        final Class<? extends I> clazz) {
        try {
            final I object = clazz.newInstance();
            final Field[] fields = clazz.getFields();
            for (final Field field : fields) {
                final String fieldName = NameUtils.getName(field, dbObject);
                final Object fieldValue = dbObject.get(fieldName);
                for (final FieldConverter converter : fieldConverters) {
                    if (converter.canDecode(fieldValue, field)) {
                        converter.decode(object, fieldValue, field, this);
                        break;
                    }
                }
            }
            return object;
        } catch (final InstantiationException | IllegalAccessException e) {
            return null;
        }
    }
}
