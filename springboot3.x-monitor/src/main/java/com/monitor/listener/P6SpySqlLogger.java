package com.monitor.listener;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Custom P6Spy SQL Logger for statistics
 */
@Slf4j
public class P6SpySqlLogger implements MessageFormattingStrategy {

    private static final ConcurrentHashMap<String, SqlStats> SQL_STATS = new ConcurrentHashMap<>();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        if (sql == null || sql.trim().isEmpty()) {
            return "";
        }
        if (Category.STATEMENT.getName().equals(category)) {
            SQL_STATS.computeIfAbsent(sql, key -> new SqlStats())
                    .recordExecution(elapsed);
            log.debug("SQL executed: [{}], elapsed={}ms", sql, elapsed);
        }
        return sql;
    }

    /**
     * Expose SQL stats map
     */
    public static ConcurrentHashMap<String, SqlStats> getSqlStats() {
        return SQL_STATS;
    }

    /**
     * SQL statistics entity
     */
    @Slf4j
    public static class SqlStats {
        private final AtomicLong count = new AtomicLong();
        private final AtomicLong totalTime = new AtomicLong();
        private volatile long maxTime = Long.MIN_VALUE;
        private volatile long minTime = Long.MAX_VALUE;

        public void recordExecution(long elapsed) {
            count.incrementAndGet();
            totalTime.addAndGet(elapsed);
            maxTime = Math.max(maxTime, elapsed);
            minTime = Math.min(minTime, elapsed);
        }

        public long getCount() {
            return count.get();
        }

        public double getAvgTime() {
            return count.get() == 0 ? 0 : (double) totalTime.get() / count.get();
        }

        public long getMaxTime() {
            return maxTime == Long.MIN_VALUE ? 0 : maxTime;
        }

        public long getMinTime() {
            return minTime == Long.MAX_VALUE ? 0 : minTime;
        }
    }
}
