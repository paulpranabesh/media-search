package com.my.company.media.service.album;

import com.my.company.media.aop.TrackTime;
import com.my.company.media.exception.ITunesApiCommunicationException;
import com.my.company.media.model.dto.itunes.album.ITunesApiResponse;
import com.my.company.media.configuration.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * Created by prpaul on 9/26/2019.
 */
@Service
public class AlbumRetrivalService {
    private final Logger logger = LoggerFactory.getLogger(AlbumRetrivalService.class);
    @Autowired
    private AppProperties config;
    @Autowired
    private RestTemplate restTemplate;
    @TrackTime
    public ITunesApiResponse retrieveAlbum(final String searchKey) throws ITunesApiCommunicationException {
        String url = MessageFormat.format(config.getiTunesAlbumApi(), albumRetrivalParams(searchKey));
        try{
            ITunesApiResponse response = restTemplate.getForObject(url, ITunesApiResponse.class);
            return response;
        }catch(HttpClientErrorException e){
            String errorMsg = "Unable to retrieve albums from iTunes music Api.";
            logger.error(errorMsg, e);
            throw new ITunesApiCommunicationException(errorMsg, e);
        }catch(Exception e){
            String errorMsg = "Communication error with iTunes Api for Music.";
            logger.error(errorMsg, e);
            throw new ITunesApiCommunicationException(errorMsg, e);
        }
    }

    private Object[] albumRetrivalParams(final String searchKey){
        return new Object[]{searchKey, "albumTerm", "album", config.getResultSize(), "en_us"};
    }
}
