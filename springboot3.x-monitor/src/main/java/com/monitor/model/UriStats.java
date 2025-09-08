package com.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * URI access statistics entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UriStats {
    private long count;
    private long errorCount;
    private double avgTime;
    private long maxTime;
    private long minTime;
}
