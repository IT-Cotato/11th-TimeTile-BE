package cotato.timetile.domain.event.application;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.global.exception.ConflictException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.TooManyRequestsException;
import cotato.timetile.global.handler.RedisHandler;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventReportService {

    private static final int RETENTION_DAY = 1;
    private static final int REPORT_POSSIBLE_LIMIT = 5;
    private final EventRepository eventRepository;
    private final RedisHandler redisHandler;

    @Transactional
    public void report(Long tileId, Long userId) {
        Event event = eventRepository.findById(tileId).orElseThrow(NotFoundException::wrong);
        String redisKey = generateRedisKey(userId);
        String redisValue = String.valueOf(tileId);
        if (redisHandler.check(redisKey, redisValue)) {
            throw ConflictException.wrong();
        }
        if (redisHandler.getAll(redisKey).size() >= REPORT_POSSIBLE_LIMIT) {
            throw TooManyRequestsException.wrong();
        }
        redisHandler.setExpiredAt(redisKey, RETENTION_DAY);
        redisHandler.add(redisKey, redisValue);
        event.increaseReportCount();
    }

    @Transactional
    public void cancelReport(Long tileId, Long userId) {
        Event event = eventRepository.findById(tileId).orElseThrow(NotFoundException::wrong);
        String redisKey = generateRedisKey(userId);
        String redisValue = String.valueOf(tileId);
        if (redisHandler.check(redisKey, redisValue)) {
            redisHandler.remove(redisKey, redisValue);
            event.decreaseReportCount();
        } else {
            throw NotFoundException.wrong();
        }
    }

    private String generateRedisKey(Long userId) {
        return String.format("report:user:%d:date:%s", userId, LocalDate.now());
    }

}
