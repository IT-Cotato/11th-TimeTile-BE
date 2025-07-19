package cotato.timetile.domain.comment.application;

import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.comment.persistence.CommentRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentRemovalService {

    private final CommentRepository commentRepository;

    @Transactional
    public void remove(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::wrong);
        if (comment.isNotWrittenBy(userId)) {
            throw ForbiddenException.wrong();
        }
        comment.getPost().decreaseCommentCount();
        comment.getCommenter().removeComment(comment);
    }

}
