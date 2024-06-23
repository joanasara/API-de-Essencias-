package com.example.apiessencias.client_essencias;

import com.example.apiessencias.exception.SearchLimitExceededException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class LimiteBusca {

    private static final int REQUEST_LIMIT = 5;
    private static final long WINDOW_SIZE = Duration.ofMinutes(1).toMillis();
    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    public void checkLimit(String clientId) {
        RequestCounter counter = requestCounts.computeIfAbsent(clientId, id -> new RequestCounter());
        if (counter.exceededLimit()) {
            throw new SearchLimitExceededException("Limite de requisições excedido. Tente novamente mais tarde.");
        }
        counter.increment();
    }

    private static class RequestCounter {
        private int count;
        private long windowStart = System.currentTimeMillis();

        public synchronized void increment() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_SIZE) {
                count = 1;
                windowStart = now;
            } else {
                count++;
            }
        }

        public synchronized boolean exceededLimit() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_SIZE) {
                count = 1;
                windowStart = now;
                return false;
            }
            return count > REQUEST_LIMIT;
        }
    }

}