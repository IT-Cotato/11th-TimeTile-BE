package cotato.timetile.domain.user.application;

import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.domain.UserFollow;
import cotato.timetile.domain.user.persistence.UserFollowRepository;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.BadRequestException;
import cotato.timetile.global.exception.ConflictException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        validateFollowRequest(followerId, followingId);
        boolean alreadyFollowed = userFollowRepository.findByFollowingIdAndFollowerId(followingId, followerId)
                .isPresent();
        if (alreadyFollowed) {
            throw ConflictException.wrong();
        }
        User following = userRepository.findById(followingId).orElseThrow(UnauthorizedException::failed);
        User follower = userRepository.findById(followerId).orElseThrow(NotFoundException::wrong);
        userFollowRepository.save(UserFollow.of(following, follower));
        following.increaseFollowingCount();
        follower.increaseFollowerCount();
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        validateFollowRequest(followerId, followingId);
        UserFollow follow = userFollowRepository.findByFollowingIdAndFollowerId(followingId, followerId)
                .orElseThrow(NotFoundException::wrong);
        User following = follow.getFollowing();
        User follower = follow.getFollower();
        following.decreaseFollowingCount();
        follower.decreaseFollowerCount();
        userFollowRepository.delete(follow);
    }

    private void validateFollowRequest(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw BadRequestException.wrong();
        }
    }

}
