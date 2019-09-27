package com.my.company.media.model.dto.itunes.album;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Created by prpaul on 9/26/2019.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ITunesApiResponse {
    @JsonProperty("results")
    private List<ITunesAlbum> results;

    public List<ITunesAlbum> getResults() {
        return results == null ? Collections.emptyList() : Collections.unmodifiableList(results);
    }

}
