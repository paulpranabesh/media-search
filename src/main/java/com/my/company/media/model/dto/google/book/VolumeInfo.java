package com.my.company.media.model.dto.google.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Collections;
import java.util.List;

/**
 * Created by prpaul on 9/26/2019.
 */
public class VolumeInfo {
     @JsonProperty("title")
     private String title;
     @JsonProperty("authors")
     private List<String> authors;
     public String getTitle() {
          return title;
     }

     public List<String> getAuthors() {
          return authors == null ? Collections.emptyList() : Collections.unmodifiableList(authors);
     }
}
