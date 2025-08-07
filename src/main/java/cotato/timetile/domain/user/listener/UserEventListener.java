package cotato.timetile.domain.user.listener;

import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.domain.UserDocument;
import cotato.timetile.domain.user.listener.dto.UserCreationEvent;
import cotato.timetile.domain.user.listener.dto.UserRemovalEvent;
import cotato.timetile.domain.user.listener.dto.UserUpdateEvent;
import cotato.timetile.domain.user.persistence.UserDocumentRepository;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreationEvent(UserCreationEvent event) {
        User user = userRepository.findById(event.id()).orElseThrow(NotFoundException::wrong);
        userDocumentRepository.save(UserDocument.of(user));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdateEvent(UserUpdateEvent event) {
        User user = userRepository.findById(event.id()).orElseThrow(NotFoundException::wrong);
        userDocumentRepository.save(UserDocument.of(user));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostRemovalEvent(UserRemovalEvent event) {
        userDocumentRepository.deleteById(event.id());
    }
    
}
