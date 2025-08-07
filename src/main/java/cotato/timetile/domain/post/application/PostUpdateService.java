package cotato.timetile.domain.post.application;

import cotato.timetile.domain.post.api.dto.PostUpdateDto;
import cotato.timetile.domain.post.api.request.PostUpdateRequest;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.listener.dto.PostUpdateEvent;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.S3Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostUpdateService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Handler s3Handler;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void update(PostUpdateRequest request, Long postId, Long userId) {
        User author = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
        if (post.isNotWrittenBy(author)) {
            throw ForbiddenException.wrong();
        }
        List<String> mediaKeys = request.mediaKeys();
        s3Handler.deleteNotAllowedFiles(mediaKeys);
        post.update(
                PostUpdateDto.of(
                        request,
                        mediaKeys.isEmpty() ? null : request.mediaKeys().get(request.mainImageIndex())
                )
        );
        applicationEventPublisher.publishEvent(PostUpdateEvent.of(post));
    }

}
