package cotato.timetile.global.handler;

import cotato.timetile.domain.event.listener.dto.EventCreationEvent;
import cotato.timetile.domain.event.persistence.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventProcessHandler {

    public static int REMOVE_THRESHOLD = 10;
    private final EventRepository eventRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void removeIfWorthlessElseActivate(Long eventId) {
        eventRepository.findById(eventId).ifPresent(event -> {
            if (event.getReportCount() >= REMOVE_THRESHOLD) {
                eventRepository.deleteById(eventId);
            } else {
                event.activate();
                applicationEventPublisher.publishEvent(EventCreationEvent.of(event));
            }
        });
    }

}
