package com.my.company.media.model.dto.google.book;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by prpaul on 9/26/2019.
 */
public class Item {
    @JsonProperty("volumeInfo")
    private VolumeInfo volumeInfo;

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
}
