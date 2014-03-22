package com.btmatthews.atlas.core.dao.mongo;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Created by bmatthews68 on 22/03/2014.
 */
public class PersonDAOImpl extends MongoDAO<String, Person> {

    public PersonDAOImpl(final MongoClient mongoClient) {
        super(mongoClient, "db", "people");
    }

    @Override
    protected void marshal(final DBObject dbObject,
                           final Person object) {
        dbObject.put("_id", object.getId());
        dbObject.put("name", object.getName());
    }

    @Override
    protected Person unmarshal(final DBObject dbObject) {
        final String id = (String) dbObject.get("_id");
        final String name = (String) dbObject.get("name");
        return new PersonImpl(id, name);
    }
}
