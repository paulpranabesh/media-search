package com.my.company.media.model.dto.itunes.album;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by prpaul on 9/26/2019.
 */
public class ITunesAlbum {
    @JsonProperty("artistName")
    private String artist;
    @JsonProperty("collectionName")
    private String collectionName;

    public String getCollectionName() {
        return collectionName;
    }

    public String getArtist() {
        return artist;
    }
}
