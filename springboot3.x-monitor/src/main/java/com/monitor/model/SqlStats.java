package com.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SQL execution statistics entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlStats {
    private long count;
    private double avgTime;
    private long maxTime;
    private long minTime;
}
