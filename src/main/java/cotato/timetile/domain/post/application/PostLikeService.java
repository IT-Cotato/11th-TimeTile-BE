package cotato.timetile.domain.post.application;

import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.PostLike;
import cotato.timetile.domain.post.persistence.PostLikeRepository;
import cotato.timetile.domain.post.persistence.PostRepository;
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
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        boolean alreadyLiked = user.getPostLikes().stream()
                .anyMatch(pl -> pl.getPost().getId().equals(postId));
        if (alreadyLiked) {
            throw ConflictException.wrong();
        }
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
        user.like(PostLike.of(user, post));
        post.increaseLikeCount();
    }

    @Transactional
    public void cancelLike(Long postId, Long userId) {
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(NotFoundException::wrong);
        postLike.getUser().cancelLike(postLike);
        postLike.getPost().decreaseLikeCount();
    }

}
