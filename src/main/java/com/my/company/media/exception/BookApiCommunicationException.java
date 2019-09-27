package com.my.company.media.exception;

/**
 * Created by prpaul on 9/27/2019.
 */
public class BookApiCommunicationException extends Exception {
    public BookApiCommunicationException(String msg, Exception e){
        super(msg, e);
    }
}
