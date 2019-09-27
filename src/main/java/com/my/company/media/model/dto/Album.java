package com.my.company.media.model.dto;

/**
 * Created by prpaul on 9/26/2019.
 */
public class Album extends Media {
    private final String artist;

    public Album(String artist, String title) {
        super(title);
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

}
