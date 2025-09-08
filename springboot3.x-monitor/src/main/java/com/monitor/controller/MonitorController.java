package com.monitor.controller;


import com.monitor.model.SqlStats;
import com.monitor.model.UriStats;
import com.monitor.service.SqlStatsService;
import com.monitor.service.UriStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for exposing monitor statistics
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MonitorController {

    private final SqlStatsService sqlStatsService;
    private final UriStatsService uriStatsService;

    @GetMapping("/monitor/sql")
    public Map<String, SqlStats> getSqlStats() {
        log.info("Fetching SQL statistics...");
        return sqlStatsService.getAllSqlStats();
    }

    @GetMapping("/monitor/uri")
    public Map<String, UriStats> getUriStats() {
        log.info("Fetching URI statistics...");
        return uriStatsService.getAllUriStats();
    }

    @GetMapping("/monitor/uri2")
    public Map<String, UriStats> getUriStats2() {
        log.info("Fetching URI statistics...");
        return uriStatsService.getAllUriStats();
    }
}
