package cotato.timetile.domain.profile.application;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.artist.domain.ArtistFollow;
import cotato.timetile.domain.artist.domain.QArtistFollow;
import cotato.timetile.domain.profile.api.dto.FollowProfileDto;
import cotato.timetile.domain.profile.api.response.FollowProfileResponse;
import cotato.timetile.domain.user.domain.QUserFollow;
import cotato.timetile.domain.user.domain.UserFollow;
import cotato.timetile.global.handler.S3Handler;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowProfileService {

    private static final int SLICE_SIZE = 20;
    private final JPAQueryFactory jpaQueryFactory;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public FollowProfileResponse loadFollowerUsers(Long targetId, Long lastFollowId) {
        QUserFollow uf = QUserFollow.userFollow;
        return loadFollows(
                uf,
                uf.follower.id.eq(targetId),
                uf.id.lt(lastFollowId),
                UserFollow::getFollowing,
                follower -> FollowProfileDto.of(
                        follower.getId().toString(),
                        follower.getNickname(),
                        s3Handler.getSimpleLogoUrlIfNull(follower.getImageKey())
                ),
                UserFollow::getId
        );
    }

    @Transactional(readOnly = true)
    public FollowProfileResponse loadFollowingUsers(Long targetId, Long lastFollowId) {
        QUserFollow uf = QUserFollow.userFollow;
        return loadFollows(
                uf,
                uf.following.id.eq(targetId),
                uf.id.lt(lastFollowId),
                UserFollow::getFollower,
                following -> FollowProfileDto.of(
                        following.getId().toString(),
                        following.getNickname(),
                        s3Handler.getSimpleLogoUrlIfNull(following.getImageKey())
                ),
                UserFollow::getId
        );
    }

    @Transactional(readOnly = true)
    public FollowProfileResponse loadFollowingArtists(Long targetId, Long lastFollowId) {
        QArtistFollow af = QArtistFollow.artistFollow;
        return loadFollows(
                af,
                af.user.id.eq(targetId),
                af.id.lt(lastFollowId),
                ArtistFollow::getArtist,
                artist -> FollowProfileDto.of(
                        artist.getId(),
                        artist.getName(),
                        artist.getImageUrl()
                ),
                ArtistFollow::getId
        );
    }

    private <F, T> FollowProfileResponse loadFollows(EntityPathBase<F> entity,
                                                     BooleanExpression baseCondition,
                                                     BooleanExpression cursorCondition,
                                                     Function<F, T> extractTarget,
                                                     Function<T, FollowProfileDto> toDto,
                                                     Function<F, Long> getCursorId) {
        List<F> fetched = jpaQueryFactory
                .selectFrom(entity)
                .where(baseCondition, cursorCondition)
                .orderBy(Expressions.numberPath(Long.class, "id").desc())
                .limit(SLICE_SIZE + 1)
                .fetch();

        boolean hasNext = fetched.size() > SLICE_SIZE;
        List<F> follows = hasNext ? fetched.subList(0, SLICE_SIZE) : fetched;

        Optional<F> lastFollowOpt = follows.isEmpty()
                ? Optional.empty()
                : Optional.of(follows.get(follows.size() - 1));

        List<FollowProfileDto> dtoList = follows.stream()
                .map(extractTarget)
                .map(toDto)
                .toList();

        Long nextCursorId = lastFollowOpt.map(getCursorId).orElse(null);

        return FollowProfileResponse.of(dtoList, hasNext, nextCursorId);
    }

}
