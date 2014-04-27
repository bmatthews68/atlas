package com.btmatthews.atlas.core.dao.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ObjectWithTime {

    private LocalDateTime dateTime;

    @JsonCreator
    public ObjectWithTime(@JsonProperty("dateTime") final LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
