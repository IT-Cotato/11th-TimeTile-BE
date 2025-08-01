package cotato.timetile.global.handler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public boolean expire(String key, Duration timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void add(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public boolean check(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    public Set<String> getAll(String key) {
        return redisTemplate.opsForSet().members(key).stream().map(Object::toString).collect(Collectors.toSet());
    }

    public void remove(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public void setExpiredAt(String key, int day) {
        redisTemplate.expireAt(key, Date.from(
                LocalDate.now().plusDays(day).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        );
    }

    public void zAdd(String key, Object value, long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public void zRemove(String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public void zRemoveRange(String key, long start, long end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    public List<String> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end).stream()
                .map(Object::toString)
                .toList();
    }

}
