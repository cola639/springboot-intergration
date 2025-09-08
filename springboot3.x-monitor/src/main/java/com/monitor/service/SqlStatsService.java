package com.monitor.service;


import com.monitor.listener.P6SpySqlLogger;
import com.monitor.model.SqlStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for SQL statistics
 */
@Slf4j
@Service
public class SqlStatsService {

    /**
     * Get all SQL statistics
     */
    public Map<String, SqlStats> getAllSqlStats() {
        log.debug("Collecting SQL statistics...");
        return P6SpySqlLogger.getSqlStats().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new SqlStats(
                                e.getValue().getCount(),
                                e.getValue().getAvgTime(),
                                e.getValue().getMaxTime(),
                                e.getValue().getMinTime()
                        )
                ));
    }
}
