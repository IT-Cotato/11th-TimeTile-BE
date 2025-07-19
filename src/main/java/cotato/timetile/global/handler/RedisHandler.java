package cotato.timetile.global.handler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
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

}
