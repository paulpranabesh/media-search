package com.my.company.media.exception;

import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by prpaul on 9/27/2019.
 */
@ResponseBody
public class NoMediaFoundException extends Exception {
    public NoMediaFoundException(String msg){
        super(msg);
    }
}
