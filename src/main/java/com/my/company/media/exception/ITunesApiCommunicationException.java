package com.my.company.media.exception;

/**
 * Created by prpaul on 9/27/2019.
 */
public class ITunesApiCommunicationException extends Exception{

    public ITunesApiCommunicationException(String msg, Exception e){
        super(msg, e);
    }
}
