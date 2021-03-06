package com.btmatthews.atlas.core.domain.jsr310;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
