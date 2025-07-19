package cotato.timetile.domain.comment.application;

import cotato.timetile.domain.comment.api.dto.CommentUpdateDto;
import cotato.timetile.domain.comment.api.request.CommentUpdateRequest;
import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.comment.persistence.CommentRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentUpdateService {

    private final CommentRepository commentRepository;

    @Transactional
    public void update(CommentUpdateRequest request, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::wrong);
        if (comment.isNotWrittenBy(userId)) {
            throw ForbiddenException.wrong();
        }
        comment.update(CommentUpdateDto.of(request));
    }

}
