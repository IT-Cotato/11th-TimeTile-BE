package cotato.timetile.domain.post.listener;

import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.PostDocument;
import cotato.timetile.domain.post.listener.dto.PostCreationEvent;
import cotato.timetile.domain.post.listener.dto.PostRemovalEvent;
import cotato.timetile.domain.post.listener.dto.PostUpdateEvent;
import cotato.timetile.domain.post.persistence.PostDocumentRepository;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final PostRepository postRepository;
    private final PostDocumentRepository postDocumentRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreationEvent(PostCreationEvent event) {
        Post post = postRepository.findById(event.id()).orElseThrow(NotFoundException::wrong);
        postDocumentRepository.save(PostDocument.of(post));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdateEvent(PostUpdateEvent event) {
        Post post = postRepository.findById(event.id()).orElseThrow(NotFoundException::wrong);
        postDocumentRepository.save(PostDocument.of(post));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostRemovalEvent(PostRemovalEvent event) {
        postDocumentRepository.deleteById(event.id());
    }

}

