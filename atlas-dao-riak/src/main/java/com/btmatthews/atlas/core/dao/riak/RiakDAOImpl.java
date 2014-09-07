package com.btmatthews.atlas.core.dao.riak;


import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.Quorum;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.kv.DeleteValue;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.MultiFetch;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;
import com.btmatthews.atlas.core.dao.DAO;
import com.btmatthews.atlas.core.dao.DataAccessException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RiakDAOImpl<ID, I, T extends I> implements DAO<ID, I> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiakDAOImpl.class);

    private static final String ERROR_CANNOT_CONVERT_TO_JSON = "";

    private static final String ERROR_FAILED_TO_STORE_VALUE_IN_RIAK = "";

    private static final String ERROR_STORING_VALUE_IN_RIAK_INTERRUPPTED = "";

    private final RiakClient client;

    private final ObjectMapper objectMapper;

    private final Namespace namespace;

    private final Quorum quorum;

    private final Class<I> objectClass;

    private final int timeout;

    public RiakDAOImpl(final RiakClient client,
                       final ObjectMapper objectMapper,
                       final String bucketName,
                       final Class<I> objectClass,
                       final int timeout) {
        this(client, objectMapper, bucketName, objectClass, timeout, 1);
    }

    public RiakDAOImpl(final RiakClient client,
                       final ObjectMapper objectMapper,
                       final String bucketName,
                       final Class<I> objectClass,
                       final int timeout,
                       final int quorum) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.namespace = new Namespace("default", bucketName);
        this.objectClass = objectClass;
        this.timeout = timeout;
        this.quorum = new Quorum(quorum);
    }

    @Override
    public void create(final ID id, final I obj) {
        StoreValue store = new StoreValue.Builder(toRiakObject(obj))
                .withLocation(location(id))
                .withOption(StoreValue.Option.IF_NONE_MATCH, Boolean.TRUE)
                .withOption(StoreValue.Option.W, quorum)
                .withTimeout(timeout)
                .build();
        try {
            client.execute(store);
        } catch (final ExecutionException e) {
            final String message = ERROR_FAILED_TO_STORE_VALUE_IN_RIAK;
            LOGGER.error(message, e);
            throw new DataAccessException(message, e);
        } catch (final InterruptedException e) {
            final String message = ERROR_STORING_VALUE_IN_RIAK_INTERRUPPTED;
            LOGGER.error(message);
            throw new DataAccessException(message, e);
        }
    }

    @Override
    public Optional<I> read(final ID id) {

        final FetchValue fetch = new FetchValue.Builder(location(id))
                .withOption(FetchValue.Option.NOTFOUND_OK, Boolean.TRUE)
                .withTimeout(timeout)
                .build();
        try {
            final FetchValue.Response response = client.execute(fetch);
            return fromFetchValueResponse(response);
        } catch (final ExecutionException e) {
            throw new DataAccessException("", e);
        } catch (final InterruptedException e) {
            throw new DataAccessException("", e);
        }
    }

    @Override
    public List<Optional<I>> read(final ID... ids) {
        final MultiFetch multiFetch = new MultiFetch.Builder()
                .addLocations(Lists.transform(Arrays.asList(ids), id -> location(id)))
                .withOption(FetchValue.Option.NOTFOUND_OK, Boolean.TRUE)
                .withTimeout(timeout)
                .build();
        try {
            MultiFetch.Response response = client.execute(multiFetch);
            return Lists.transform(response.getResponses(), f -> {
                try {
                    return fromFetchValueResponse(f.get());
                } catch (final InterruptedException e) {
                    throw new DataAccessException("", e);
                }
            });
        } catch (final ExecutionException e) {
            throw new DataAccessException("", e);
        } catch (final InterruptedException e) {
            throw new DataAccessException("", e);
        }
    }

    @Override
    public void update(final ID id, final I obj) {
        final StoreValue storeValue = new StoreValue.Builder(toRiakObject(obj))
                .withLocation(location(id))
                .withOption(StoreValue.Option.IF_NONE_MATCH, Boolean.TRUE)
                .withTimeout(timeout)
                .build();
        try {
            client.execute(storeValue);
        } catch (final ExecutionException e) {
            throw new DataAccessException("", e);
        } catch (final InterruptedException e) {
            throw new DataAccessException("", e);
        }
    }

    @Override
    public void destroy(final ID id) {
        final DeleteValue deleteValue = new DeleteValue.Builder(location(id))
                .withTimeout(timeout)
                .build();
        try {
            client.execute(deleteValue);
        } catch (final ExecutionException e) {
            throw new DataAccessException("", e);
        } catch (final InterruptedException e) {
            throw new DataAccessException("", e);
        }
    }

    private Location location(final ID id) {
        return new Location(namespace, id.toString());
    }

    private String asJSON(final I obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (final JsonProcessingException e) {
            final String message = ERROR_CANNOT_CONVERT_TO_JSON;
            LOGGER.error(message, e);
            throw new DataAccessException(message, e);
        }
    }

    private I asObject(final String json) {
        try {
            return objectMapper.readValue(json, objectClass);
        } catch (IOException e) {
            throw new DataAccessException("", e);
        }
    }

    private RiakObject toRiakObject(final I obj) {
        final RiakObject object = new RiakObject();
        object.setValue(BinaryValue.create(asJSON(obj)));
        return object;
    }

    private I fromRiakObject(final RiakObject riakObject) {
        final BinaryValue value = riakObject.getValue();
        return asObject(new String(value.getValue()));
    }

    private Optional<I> fromFetchValueResponse(final FetchValue.Response response) {
        if (response.isNotFound()) {
            return Optional.empty();
        } else {
            try {
                final RiakObject riakObject = response.getValue(RiakObject.class);
                return Optional.of(fromRiakObject(riakObject));
            } catch (final UnresolvedConflictException e) {
                throw new DataAccessException("", e);
            }
        }
    }

    private class FutureWrapper implements Future<I> {

        private final Future<String> delegate;

        public FutureWrapper(final Future<String> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return delegate.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public boolean isDone() {
            return delegate.isDone();
        }

        @Override
        public I get() throws InterruptedException, ExecutionException {
            final String json = delegate.get();
            try {
                return objectMapper.readValue(json, objectClass);
            } catch (final JsonMappingException e) {
                throw new ExecutionException(e);
            } catch (final JsonParseException e) {
                throw new ExecutionException(e);
            } catch (final IOException e) {
                throw new ExecutionException(e);
            }
        }

        @Override
        public I get(final long timeout,
                     final TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            final String json = delegate.get(timeout, unit);
            try {
                return objectMapper.readValue(json, objectClass);
            } catch (final JsonMappingException e) {
                throw new ExecutionException(e);
            } catch (final JsonParseException e) {
                throw new ExecutionException(e);
            } catch (final IOException e) {
                throw new ExecutionException(e);
            }
        }
    }
}
