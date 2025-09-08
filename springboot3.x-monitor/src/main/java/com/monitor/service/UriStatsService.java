package com.monitor.service;


import com.monitor.interceptor.UriStatsInterceptor;
import com.monitor.model.UriStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for URI statistics
 */
@Slf4j
@Service
public class UriStatsService {

    /**
     * Get all URI statistics
     */
    public Map<String, UriStats> getAllUriStats() {
        log.debug("Collecting URI statistics...");
        return UriStatsInterceptor.getUriStats().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new UriStats(
                                e.getValue().getCount(),
                                e.getValue().getErrorCount(),
                                e.getValue().getAvgTime(),
                                e.getValue().getMaxTime(),
                                e.getValue().getMinTime()
                        )
                ));
    }
}
