package com.my.company.media.metric;

import java.util.Date;

/**
 * Created by prpaul on 9/27/2019.
 */
public class ExternalApiAccessData {
    private  Date lastAccess;
    private  long hitCount;
    private  long totalAccessTime;

    public ExternalApiAccessData() {
        this.hitCount = 0l;
        this.totalAccessTime = 0l;
    }

    public  synchronized void update(long currentAccessTime){
        totalAccessTime+=currentAccessTime;
        hitCount++;
        lastAccess = new Date();
    }

    public synchronized long avgAccessTime(){
        if(hitCount == 0l){
            return 0l;
        }
        return totalAccessTime/hitCount;
    }

}
