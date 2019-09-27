package com.my.company.media.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by prpaul on 9/26/2019.
 */
@Component
public class AppProperties {

    @Value("${media.result.size}")
    private int resultSize;

    @Value("${media.google.book.api}")
    private String googleBookApi;

    @Value("${media.itunes.album.api}")
    private String iTunesAlbumApi;

    public String getiTunesAlbumApi() {
        return iTunesAlbumApi;
    }

    public String getGoogleBookApi() {
        return googleBookApi;
    }

    public int getResultSize() {
        return resultSize;
    }
}
