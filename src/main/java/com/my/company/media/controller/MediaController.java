package com.my.company.media.controller;

import com.my.company.media.exception.NoMediaFoundException;
import com.my.company.media.model.dto.Media;
import com.my.company.media.service.MediaAggregatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prpaul on 9/26/2019.
 */
@RestController()
@RequestMapping("media")
public class MediaController {
    private final Logger logger = LoggerFactory.getLogger(MediaController.class);
    @Autowired
    private MediaAggregatorService mediaAggregator;

    @GetMapping(path = "/list/{searchKey}",produces = "application/json")
    public List<Media> retrieveInformation(@PathVariable final String searchKey){
        try {
            return mediaAggregator.searchMedia(searchKey);
        } catch (NoMediaFoundException e) {
            logger.error("No media informations found.", e);
            throw new ResponseStatusException(
                 HttpStatus.NOT_FOUND, "Unable to retrieve any media information with search key :"+searchKey, e);
        }
    }
}
