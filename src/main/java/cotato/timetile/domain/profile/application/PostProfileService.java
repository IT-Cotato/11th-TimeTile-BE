package cotato.timetile.domain.profile.application;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.QEvent;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.QPost;
import cotato.timetile.domain.post.domain.QPostLike;
import cotato.timetile.domain.profile.api.dto.PostLoadAllOnProfileDto;
import cotato.timetile.domain.profile.api.dto.PostWithAuthorLoadAllOnProfileDto;
import cotato.timetile.domain.profile.api.response.PostLoadAllOnLimitResponse;
import cotato.timetile.domain.profile.api.response.PostLoadAllOnPageResponse;
import cotato.timetile.domain.profile.api.response.PostLoadAllOnSliceResponse;
import cotato.timetile.domain.profile.api.response.PostWithAuthorLoadAllOnPageResponse;
import cotato.timetile.domain.scrap.domain.QScrap;
import cotato.timetile.domain.scrap.persistence.ScrapRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserFollowRepository;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.common.Visibility;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.handler.S3Handler;
import cotato.timetile.global.helper.QuerydslHelper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostProfileService {

    private static final int POST_PAGE_SIZE = 10;
    private static final int POST_ON_PROFILE_SIZE = 10;
    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final QuerydslHelper querydslHelper;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public PostLoadAllOnLimitResponse loadPostsOnMyProfile(Long userId) {

        QPost p = QPost.post;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        BooleanExpression condition = p.author.id.eq(userId);

        List<Tuple> fetched = querydslHelper.joinLatestActiveEvent(e1, e2, p, p.groupId)
                .where(condition)
                .orderBy(p.id.desc())
                .limit(POST_ON_PROFILE_SIZE)
                .fetch();

        return PostLoadAllOnLimitResponse.of(
                fetched.stream()
                        .map(tuple -> {
                            Post post = Objects.requireNonNull(tuple.get(p));
                            Event event = Objects.requireNonNull(tuple.get(e1));
                            return PostLoadAllOnProfileDto.of(
                                    post,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                    event,
                                    scrapRepository.existsByScrapFolder_Creator_IdAndPost_Id(userId, post.getId())
                            );
                        }).toList()
        );
    }

    @Transactional(readOnly = true)
    public PostLoadAllOnSliceResponse loadPostsOnOtherUserProfile(Long targetId, Long lastPostId, Long userId) {
        User target = userRepository.findById(targetId).orElseThrow(NotFoundException::wrong);
        validateAccessPermission(target, userId);

        QPost p = QPost.post;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        BooleanExpression baseCondition = p.author.id.eq(targetId);
        BooleanExpression cursorCondition = p.id.lt(lastPostId);

        List<Tuple> tuples = querydslHelper.joinLatestActiveEvent(e1, e2, p, p.groupId)
                .where(baseCondition, cursorCondition)
                .orderBy(p.id.desc())
                .fetch();

        boolean hasNext = tuples.size() > POST_ON_PROFILE_SIZE;
        tuples = hasNext ? tuples.subList(0, POST_ON_PROFILE_SIZE) : tuples;
        Optional<Tuple> lastTupleOpt = tuples.isEmpty() ? Optional.empty() : Optional.of(tuples.get(tuples.size() - 1));

        return PostLoadAllOnSliceResponse.of(
                tuples.stream()
                        .map(tuple -> {
                            Post post = Objects.requireNonNull(tuple.get(p));
                            Event event = Objects.requireNonNull(tuple.get(e1));
                            return PostLoadAllOnProfileDto.of(
                                    post,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                    event,
                                    scrapRepository.existsByScrapFolder_Creator_IdAndPost_Id(userId, post.getId())
                            );
                        }).toList(),
                hasNext,
                lastTupleOpt.map(tuple -> Objects.requireNonNull(tuple.get(p)).getId()).orElse(null)
        );
    }

    @Transactional(readOnly = true)
    public PostLoadAllOnPageResponse loadMyPosts(int page, Visibility visibility, Long userId) {
        QPost p = QPost.post;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        PageRequest pageable = PageRequest.of(page - 1, POST_PAGE_SIZE);

        BooleanExpression condition = p.author.id.eq(userId).and(p.visibility.eq(visibility));

        List<Tuple> tuples = querydslHelper.joinLatestActiveEvent(e1, e2, p, p.groupId)
                .where(condition)
                .orderBy(p.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(p, p.id, condition);

        return PostLoadAllOnPageResponse.of(
                tuples.stream()
                        .map(t -> {
                            Post post = t.get(p);
                            Event event = t.get(e1);
                            return PostLoadAllOnProfileDto.of(
                                    Objects.requireNonNull(post),
                                    s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                    Objects.requireNonNull(event),
                                    scrapRepository.existsByScrapFolder_Creator_IdAndPost_Id(userId, post.getId())
                            );
                        }).toList(),
                new PageImpl<>(tuples, pageable, total)
        );
    }

    @Transactional(readOnly = true)
    public PostWithAuthorLoadAllOnPageResponse loadLikedPosts(int page, Long userId) {
        QPostLike pl = QPostLike.postLike;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        PageRequest pageable = PageRequest.of(page - 1, POST_PAGE_SIZE);

        BooleanExpression condition = pl.user.id.eq(userId);

        List<Tuple> tuples = querydslHelper.joinLatestActiveEvent(e1, e2, pl, pl.post.groupId)
                .where(condition)
                .orderBy(pl.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(pl, pl.id, condition);

        return PostWithAuthorLoadAllOnPageResponse.of(
                tuples.stream()
                        .map(tuple -> {
                            Post post = Objects.requireNonNull(tuple.get(pl)).getPost();
                            Event event = Objects.requireNonNull(tuple.get(e1));
                            return PostWithAuthorLoadAllOnProfileDto.of(
                                    post,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                    event,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey()),
                                    scrapRepository.existsByScrapFolder_Creator_IdAndPost_Id(userId, post.getId())
                            );
                        }).toList(),
                new PageImpl<>(tuples, pageable, total)
        );
    }

    @Transactional(readOnly = true)
    public PostWithAuthorLoadAllOnPageResponse loadScrappedPosts(int page, Long userId) {
        QScrap s = QScrap.scrap;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        PageRequest pageable = PageRequest.of(page - 1, POST_PAGE_SIZE);

        BooleanExpression condition = s.scrapFolder.creator.id.eq(userId);

        List<Tuple> tuples = querydslHelper.joinLatestActiveEvent(e1, e2, s, s.post.groupId)
                .where(condition)
                .orderBy(s.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(s, s.id, condition);

        return PostWithAuthorLoadAllOnPageResponse.of(
                tuples.stream()
                        .map(tuple -> {
                            Post post = Objects.requireNonNull(Objects.requireNonNull(tuple.get(s)).getPost());
                            Event event = Objects.requireNonNull(tuple.get(e1));
                            return PostWithAuthorLoadAllOnProfileDto.of(
                                    post,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                    event,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey()),
                                    true
                            );
                        }).toList(),
                new PageImpl<>(tuples, pageable, total)
        );
    }

    @Transactional(readOnly = true)
    public PostWithAuthorLoadAllOnPageResponse loadScrappedPostsInFolder(Long scrapFolderId, int page, Long userId) {
        QScrap s = QScrap.scrap;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        BooleanExpression condition = s.scrapFolder.creator.id.eq(userId).and(s.scrapFolder.id.eq(scrapFolderId));

        PageRequest pageable = PageRequest.of(page - 1, POST_PAGE_SIZE);

        List<Tuple> tuples = querydslHelper.joinLatestActiveEvent(e1, e2, s, s.post.groupId)
                .where(condition)
                .orderBy(s.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(s, s.id, condition);

        return PostWithAuthorLoadAllOnPageResponse.of(
                tuples.stream()
                        .map(tuple -> {
                            Post post = Objects.requireNonNull(Objects.requireNonNull(tuple.get(s)).getPost());
                            Event event = Objects.requireNonNull(tuple.get(e1));
                            return PostWithAuthorLoadAllOnProfileDto.of(
                                    post,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                    event,
                                    s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey()),
                                    true
                            );
                        }).toList(),
                new PageImpl<>(tuples, pageable, total)
        );
    }

    private void validateAccessPermission(User target, Long userId) {
        if (target.getVisibility().equals(Visibility.PRIVATE)
                && userFollowRepository.findByFollowingIdAndFollowerId(userId, target.getId()).isEmpty()) {
            throw ForbiddenException.wrong();
        }
    }

}
