package cotato.timetile.global.handler;

import cotato.timetile.domain.event.domain.EventDocument;
import cotato.timetile.domain.event.persistence.EventDocumentRepository;
import cotato.timetile.domain.event.persistence.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventProcessHandler {

    public static int REMOVE_THRESHOLD = 10;
    private final EventRepository eventRepository;
    private final EventDocumentRepository eventDocumentRepository;

    @Transactional
    public void removeIfWorthlessElseActivate(Long eventId) {
        eventRepository.findById(eventId).ifPresent(event -> {
            if (event.getReportCount() >= REMOVE_THRESHOLD) {
                eventRepository.deleteById(eventId);
            } else {
                event.activate();
                eventDocumentRepository.save(EventDocument.of(event));
            }
        });
    }

}
