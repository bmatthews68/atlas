package com.btmatthews.atlas.jcr.oak;

import com.btmatthews.atlas.jcr.RepositoryProvider;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jcr.Repository;

@Component
public class OakRepositoryProvider implements RepositoryProvider {

    private final MongoClient mongoClient;

    private final String databaseName;

    @Autowired
    public OakRepositoryProvider(final MongoClient mongoClient,
                                 @Value("com.btmatthews.atlas.oak.db.name") final String databaseName) {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
    }

    @Override
    public Repository getRepository() {
        final DB db = mongoClient.getDB(databaseName);
        final NodeStore store = new DocumentNodeStore(new DocumentMK.Builder().setMongoDB(db));
        return new Jcr(store).createRepository();
    }
}
