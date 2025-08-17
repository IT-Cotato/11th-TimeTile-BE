package cotato.timetile.domain.home.application;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.domain.ArtistFollow;
import cotato.timetile.domain.event.api.dto.RelatedEventDto;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.QEvent;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.home.api.dto.FollowEventLoadOnHomeDto;
import cotato.timetile.domain.home.api.dto.FollowPostLoadOnHomeDto;
import cotato.timetile.domain.home.api.response.FollowEventLoadOnHomeResponse;
import cotato.timetile.domain.home.api.response.FollowPostLoadOnHomeResponse;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.QPost;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.domain.UserFollow;
import cotato.timetile.domain.user.persistence.UserFollowRepository;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.S3Handler;
import cotato.timetile.global.helper.QuerydslHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowHomeService {

    private static final int EVENT_DISPLAY_SIZE = 5;
    private static final int POST_DISPLAY_SIZE = 5;
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final EventRepository eventRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final QuerydslHelper querydslHelper;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public FollowEventLoadOnHomeResponse loadNewEvents(int page, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        List<String> artistIds = user.getArtistFollows().stream()
                .map(ArtistFollow::getArtist)
                .map(Artist::getId)
                .toList();

        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        PageRequest pageable = PageRequest.of(page - 1, EVENT_DISPLAY_SIZE);

        BooleanExpression baseCondition = e1.id.eq(
                JPAExpressions
                        .select(e2.id.max())
                        .from(e2)
                        .where(e2.groupId.eq(e1.groupId)
                                .and(e2.active.isTrue())
                                .and(e2.artist.id.in(artistIds)))
        );

        List<Event> events = jpaQueryFactory
                .selectFrom(e1)
                .where(baseCondition)
                .orderBy(e1.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(e1, e1.id, baseCondition);

        return FollowEventLoadOnHomeResponse.of(
                events.stream()
                        .map(event -> FollowEventLoadOnHomeDto.of(
                                event,
                                eventRepository.findAllByGroupIdAndActiveIsTrueAndLatest(event.getRelatedEvents())
                                        .stream()
                                        .map(RelatedEventDto::of)
                                        .toList()
                        ))
                        .toList(),
                new PageImpl<>(events, pageable, total)
        );
    }

    @Transactional(readOnly = true)
    public FollowPostLoadOnHomeResponse loadNewPosts(int page, Long userId) {
        List<Long> followerIds = userFollowRepository.findAllByFollowingId(userId).stream()
                .map(UserFollow::getFollower)
                .map(User::getId)
                .toList();

        QPost qPost = QPost.post;

        PageRequest pageable = PageRequest.of(page - 1, POST_DISPLAY_SIZE);

        BooleanExpression baseCondition = qPost.author.id.in(followerIds);

        List<Post> posts = jpaQueryFactory
                .selectFrom(qPost)
                .where(baseCondition)
                .orderBy(qPost.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(qPost, qPost.id, baseCondition);

        return FollowPostLoadOnHomeResponse.of(
                posts.stream().map(post -> FollowPostLoadOnHomeDto.of(
                        post,
                        s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                        s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey())
                )).toList(),
                new PageImpl<>(posts, pageable, total)
        );
    }

}
