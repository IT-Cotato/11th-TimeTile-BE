package cotato.timetile.domain.comment.application;

import cotato.timetile.domain.comment.api.dto.CommentLoadAllDto;
import cotato.timetile.domain.comment.api.dto.ReplyDto;
import cotato.timetile.domain.comment.api.response.CommentLoadAllResponse;
import cotato.timetile.domain.comment.persistence.CommentRepository;
import cotato.timetile.global.handler.S3Handler;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLoadService {

    private static final Duration MEDIA_VALID_DURATION = Duration.ofHours(1);
    private final CommentRepository commentRepository;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public CommentLoadAllResponse loadAll(Long postId) {
        return CommentLoadAllResponse.of(
                commentRepository.findAllByPostIdOrderByTimeInfoCreatedAtAsc(postId).stream().map(comment -> {
                    String commenterProfileImageUrl = s3Handler.getSimpleLogoUrlIfNull(
                            comment.getCommenter().getImageKey()
                    );
                    List<ReplyDto> replies = comment.getChildren().stream()
                            .sorted(Comparator.comparing(child -> child.getTimeInfo().getCreatedAt()))
                            .toList()
                            .stream().map(reply -> ReplyDto.of(
                                            reply,
                                            s3Handler.getSimpleLogoUrlIfNull(reply.getCommenter().getImageKey())
                                    )
                            ).toList();
                    return CommentLoadAllDto.of(comment, commenterProfileImageUrl, replies);
                }).toList()
        );
    }

}
