package com.my.company.media.model.dto.google.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Created by prpaul on 9/26/2019.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BookApiResponse {
    @JsonProperty("items")
    private List<Item> items;

    public List<Item> getItems(){
        return items == null ? Collections.emptyList() : Collections.unmodifiableList(items);
    }
}
