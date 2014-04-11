package com.btmatthews.atlas.core.dao.couchbase;

public class PersonImpl implements Person {

    private final String id;
    private final String name;
    private final String email;

    public PersonImpl(final String id,
                      final String name,
                      final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
