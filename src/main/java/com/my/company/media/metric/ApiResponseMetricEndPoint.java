package com.my.company.media.metric;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by prpaul on 9/27/2019.
 */
@Component
@Endpoint(id = "external-api-response")
public class ApiResponseMetricEndPoint {
    private final Map<ExternalApi, ExternalApiAccessData> accessMetric = new ConcurrentHashMap<ExternalApi, ExternalApiAccessData>();

    @PostConstruct
    public void init() {
        Stream.of(ExternalApi.values()).forEach(it -> {
            accessMetric.put(it, new ExternalApiAccessData());
        });
    }

    @ReadOperation
    public Map<ExternalApi, Long> responseTimeMetric() {
        Map<ExternalApi, Long> details = new LinkedHashMap<>();
        accessMetric.entrySet().forEach(entry->{
            details.put(entry.getKey(), entry.getValue().avgAccessTime());
        });
        return details;
    }

    public void receiveAccessData(ExternalApi apiName, long accessTime){
        accessMetric.get(apiName).update(accessTime);
    }
}
