package com.btmatthews.atlas.core.dao.riak;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.MultiFetchFuture;
import com.btmatthews.atlas.core.common.Paging;
import com.btmatthews.atlas.core.dao.DAO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RiakDAOImpl<ID, I, T extends I> implements DAO<ID, I> {

    private final IRiakClient client;

    private final ObjectMapper objectMapper;

    private final String bucketName;

    private final Class<I> objectClass;

    public RiakDAOImpl(final IRiakClient client,
                       final ObjectMapper objectMapper,
                       final String bucketName,
                       final Class<I> objectClass) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.bucketName = bucketName;
        this.objectClass = objectClass;
    }

    @Override
    public void create(final ID id, final I obj) {
        try {
            final String json = objectMapper.writeValueAsString(obj);
            bucket().store(id.toString(), json).execute();
        } catch (final RiakRetryFailedException e) {
        } catch (final JsonProcessingException e) {
        }
    }

    @Override
    public Optional<I> read(final ID id) {
        try {
            final String json = bucket().fetch(id.toString(), String.class).execute();
            return Optional.of(objectMapper.readValue(json, objectClass));
        } catch (RiakRetryFailedException e) {
            return Optional.empty();
        } catch (final JsonMappingException e) {
            return Optional.empty();
        } catch (final JsonParseException e) {
            return Optional.empty();
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Optional<I>> read(final ID... ids) {
        try {
            final List<String> idsList = Lists.transform(Arrays.asList(ids), new Function<ID, String>() {
                @Override
                public String apply(final ID id) {
                    return id.toString();
                }
            });
            final List<MultiFetchFuture<String>> futuresList = bucket().multiFetch(idsList, String.class).execute();
            return Lists.transform(futuresList, new Function<MultiFetchFuture<String>, Optional<I>>() {
                @Override
                public Optional<I> apply(MultiFetchFuture<String> future) {
                    try {
                        return Optional.of(objectMapper.readValue(future.get(), objectClass));
                    } catch (final IOException e) {
                        return Optional.empty();
                    } catch (final InterruptedException e) {
                        return Optional.empty();
                    } catch (final ExecutionException e) {
                        return Optional.empty();
                    }
                }
            });
        } catch (RiakRetryFailedException e) {
            return null;
        }
    }

    @Override
    public void update(final ID id, final I obj) {
        try {
            final String json = objectMapper.writeValueAsString(obj);
            bucket().store(id.toString(), json).execute();
        } catch (final RiakRetryFailedException e) {
        } catch (final JsonProcessingException e) {
        }
    }

    @Override
    public void destroy(final ID id) {
        try {
            bucket().delete(id.toString()).execute();
        } catch (final RiakRetryFailedException e) {
        } catch (final RiakException e) {
        }
    }

    private Bucket bucket() throws RiakRetryFailedException {
        return client.fetchBucket(bucketName).execute();
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
