package cotato.timetile.domain.event.application;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventRemovalService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public void remove(Long eventId, Long userId) {
        User author = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::wrong);
        if (event.isNotWrittenBy(author)) {
            throw ForbiddenException.wrong();
        }
        author.delete(event);
    }

}
