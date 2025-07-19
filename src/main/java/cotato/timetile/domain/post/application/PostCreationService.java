package cotato.timetile.domain.post.application;

import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.post.api.dto.PostCreationDto;
import cotato.timetile.domain.post.api.request.PostCreationRequest;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.S3Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCreationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final S3Handler s3Handler;

    @Transactional
    public void create(PostCreationRequest request, Long userId) {
        User author = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Event event = eventRepository.findTopByGroupIdAndActiveIsTrueOrderByIdDesc(request.groupId())
                .orElseThrow(NotFoundException::wrong);
        List<String> mediaKeys = request.mediaKeys();
        s3Handler.deleteNotAllowedFiles(mediaKeys);
        Post post = Post.of(
                PostCreationDto.of(
                        request,
                        mediaKeys.isEmpty() ? s3Handler.getSimpleLogoUrlIfNull(null)
                                : request.mediaKeys().get(request.mainImageIndex()),
                        event.getStartedAt(),
                        author
                )
        );
        Artist artist = event.getArtist();
        event.increasePostCount();
        artist.isSubjectOf(post);
        author.write(post);
    }

}
