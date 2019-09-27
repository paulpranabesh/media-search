package com.my.company.media.model.dto;

import java.util.List;

/**
 * Created by prpaul on 9/26/2019.
 */
public class Book extends Media {
    private final List<String> authors;;

    public Book(List<String>  authors, String title) {
        super(title);
        this.authors = authors;
    }

    public List<String> getAuthors() {
        return authors;
    }


}
