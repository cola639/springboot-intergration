package com.monitor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Interceptor for URI access statistics
 */
@Slf4j
@Component
public class UriStatsInterceptor implements HandlerInterceptor {

    private static final ConcurrentHashMap<String, UriStats> URI_STATS = new ConcurrentHashMap<>();

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        START_TIME.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        long elapsed = System.currentTimeMillis() - START_TIME.get();
        START_TIME.remove();

        String uri = request.getRequestURI();
        boolean error = (ex != null || response.getStatus() >= 400);

        URI_STATS.computeIfAbsent(uri, key -> new UriStats())
                .recordAccess(elapsed, error);

        log.debug("URI [{}] executed in {}ms, error={}", uri, elapsed, error);
    }

    /**
     * Expose URI stats map
     */
    public static ConcurrentHashMap<String, UriStats> getUriStats() {
        return URI_STATS;
    }

    /**
     * URI statistics entity
     */
    public static class UriStats {
        private final AtomicLong count = new AtomicLong();
        private final AtomicLong errorCount = new AtomicLong();
        private final AtomicLong totalTime = new AtomicLong();
        private volatile long maxTime = Long.MIN_VALUE;
        private volatile long minTime = Long.MAX_VALUE;

        public void recordAccess(long elapsed, boolean error) {
            count.incrementAndGet();
            totalTime.addAndGet(elapsed);
            if (error) {
                errorCount.incrementAndGet();
            }
            maxTime = Math.max(maxTime, elapsed);
            minTime = Math.min(minTime, elapsed);
        }

        public long getCount() {
            return count.get();
        }

        public long getErrorCount() {
            return errorCount.get();
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
