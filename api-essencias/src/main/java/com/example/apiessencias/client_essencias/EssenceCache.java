package com.example.apiessencias.client_essencias;

import com.example.apiessencias.dto.EssenciaResponseDto;
import com.example.apiessencias.dto.EssenciasResponseDto;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

@Component
public class EssenceCache {

    private static final long CACHE_EXPIRATION_TIME = Duration.ofMinutes(30).toMillis();
    private final ConcurrentHashMap<String, CacheEntry<List<EssenciasResponseDto>>> essencesCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CacheEntry<EssenciaResponseDto>> essenceByIdCache = new ConcurrentHashMap<>();

    public EssenceCache() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::clearExpiredCache, CACHE_EXPIRATION_TIME, CACHE_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    public List<EssenciasResponseDto> getEssences(String key) {
        CacheEntry<List<EssenciasResponseDto>> entry = essencesCache.get(key);
        if (entry != null && entry.isExpired()) {
            return entry.getData();
        }
        return null;
    }

    public void putEssences(String key, List<EssenciasResponseDto> data) {
        essencesCache.put(key, new CacheEntry<>(data, Instant.now().toEpochMilli()));
    }

    public EssenciaResponseDto getEssenceById(String key) {
        CacheEntry<EssenciaResponseDto> entry = essenceByIdCache.get(key);
        if (entry != null && entry.isExpired()) {
            return entry.getData();
        }
        return null;
    }

    public void putEssenceById(String key, EssenciaResponseDto data) {
        essenceByIdCache.put(key, new CacheEntry<>(data, Instant.now().toEpochMilli()));
    }

    private void clearExpiredCache() {
        long now = Instant.now().toEpochMilli();
        essencesCache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        essenceByIdCache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }

    private static class CacheEntry<T> {
        @Getter
        private final T data;
        private final long timestamp;

        public CacheEntry(T data, long timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }

        public boolean isExpired() {
            return !isExpired(Instant.now().toEpochMilli());
        }

        public boolean isExpired(long now) {
            return now - timestamp > CACHE_EXPIRATION_TIME;
        }
    }
}