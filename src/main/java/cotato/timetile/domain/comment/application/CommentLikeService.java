package cotato.timetile.domain.comment.application;

import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.comment.domain.CommentLike;
import cotato.timetile.domain.comment.persistence.CommentLikeRepository;
import cotato.timetile.domain.comment.persistence.CommentRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.ConflictException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(Long commentId, Long userId) {
        if (commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isPresent()) {
            throw ConflictException.wrong();
        }
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::wrong);
        user.like(CommentLike.of(user, comment));
        comment.increaseLikeCount();
    }

    @Transactional
    public void cancelLike(Long postId, Long userId) {
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(postId, userId)
                .orElseThrow(NotFoundException::wrong);
        commentLike.getUser().cancelLike(commentLike);
        commentLike.getComment().decreaseLikeCount();
    }
}
