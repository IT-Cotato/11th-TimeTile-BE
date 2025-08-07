package cotato.timetile.domain.event.listener;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.EventDocument;
import cotato.timetile.domain.event.listener.dto.EventCreationEvent;
import cotato.timetile.domain.event.listener.dto.EventRemovalEvent;
import cotato.timetile.domain.event.listener.dto.EventUpdateEvent;
import cotato.timetile.domain.event.persistence.EventDocumentRepository;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventEventListener {

    private final EventRepository eventRepository;
    private final EventDocumentRepository eventDocumentRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventCreationEvent(EventCreationEvent creationEvent) {
        Event event = eventRepository.findByIdWithArtist(creationEvent.id()).orElseThrow(NotFoundException::wrong);
        eventDocumentRepository.save(EventDocument.of(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventUpdateEvent(EventUpdateEvent updateEvent) {
        Event event = eventRepository.findByIdWithArtist(updateEvent.id()).orElseThrow(NotFoundException::wrong);
        eventDocumentRepository.deleteAllByGroupId(event.getGroupId());
        eventDocumentRepository.save(EventDocument.of(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventRemovalEvent(EventRemovalEvent removalEvent) {
        eventDocumentRepository.deleteById(removalEvent.id());
    }

}

