package com.my.company.media.service.book;

import com.my.company.media.aop.TrackTime;
import com.my.company.media.configuration.AppProperties;
import com.my.company.media.exception.BookApiCommunicationException;
import com.my.company.media.model.dto.google.book.BookApiResponse;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Created by prpaul on 9/26/2019.
 */
@Service
public class BookRetrivalService {
    private final Logger logger = LoggerFactory.getLogger(BookRetrivalService.class);
    @Autowired
    private AppProperties config;
    @Autowired
    private RestTemplate restTemplate;

    @TrackTime
    public BookApiResponse retrieveBook(final String searchKey) throws BookApiCommunicationException {
        notEmpty(searchKey, "'searchKey' cannot be null or empty");
        String url = MessageFormat.format(config.getGoogleBookApi(), bookRetrivalParams(searchKey));
        try{
            BookApiResponse response = restTemplate.getForObject(url, BookApiResponse.class);
            return response;
        }catch(HttpClientErrorException e){
            String errorMsg = "Unable to retrieve book informations from Google Book Api.";
            logger.error(errorMsg, e);
            throw new BookApiCommunicationException(errorMsg, e);
        }catch(Exception e){
            String errorMsg = "Communication error with Google Book Api.";
            logger.error(errorMsg, e);
            throw new BookApiCommunicationException(errorMsg, e);
        }
    }

    private Object[] bookRetrivalParams(final String searchKey){
        return new Object[]{searchKey, "english", config.getResultSize(), "relevance"};
    }
}
