package cotato.timetile.domain.post.application;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.persistence.PostRepository;
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
public class PostRemovalService {

    private final PostRepository postRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public void remove(Long postId, Long userId) {
        User author = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
        if (post.isNotWrittenBy(author)) {
            throw ForbiddenException.wrong();
        }
        Event event = eventRepository.findTopByGroupIdAndActiveIsTrueOrderByIdDesc(post.getGroupId())
                .orElseThrow(NotFoundException::wrong);
        event.decreasePostCount();
        author.delete(post);
    }

}
