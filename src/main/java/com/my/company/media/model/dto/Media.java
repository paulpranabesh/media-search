package com.my.company.media.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by prpaul on 9/26/2019.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class),
        @JsonSubTypes.Type(value = Album.class),
})
public abstract class Media {
    private final String title;

    public Media(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
