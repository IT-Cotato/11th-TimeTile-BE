package cotato.timetile.domain.search.application;

import cotato.timetile.domain.search.api.response.RecentSearchLoadResponse;
import cotato.timetile.global.handler.RedisHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecentSearchService {

    private static final int MAX_SIZE = 10;
    private final RedisHandler redisHandler;

    public void add(String query, Long userId) {
        String key = generateKey(userId);
        long score = System.currentTimeMillis();
        redisHandler.zAdd(key, query, score);
        Long size = redisHandler.zSize(key);
        if (size > MAX_SIZE) {
            redisHandler.zRemoveRange(key, 0, size - MAX_SIZE - 1);
        }
    }

    public RecentSearchLoadResponse load(Long userId) {
        String key = generateKey(userId);
        return RecentSearchLoadResponse.of(
                redisHandler.zReverseRange(key, 0, MAX_SIZE - 1)
        );
    }

    public void remove(String query, Long userId) {
        String key = generateKey(userId);
        redisHandler.zRemove(key, query);
    }

    public void clear(Long userId) {
        String key = generateKey(userId);
        redisHandler.delete(key);
    }

    private String generateKey(Long userId) {
        return "user:" + userId + ":recent_search";
    }

}
