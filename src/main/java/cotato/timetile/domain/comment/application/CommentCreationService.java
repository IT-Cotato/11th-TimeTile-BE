package cotato.timetile.domain.comment.application;

import cotato.timetile.domain.comment.api.dto.CommentCreationDto;
import cotato.timetile.domain.comment.api.request.CommentCreationRequest;
import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.comment.persistence.CommentRepository;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCreationService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(CommentCreationRequest request, Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
        User commenter = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Comment parent = Optional.ofNullable(request.parentId())
                .map(id -> commentRepository.findById(id).orElseThrow(NotFoundException::wrong))
                .orElse(null);
        post.increaseCommentCount();
        commenter.comment(Comment.of(CommentCreationDto.of(commenter, post, parent, request.content())));
    }

}
