package cotato.timetile.domain.home.application;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.home.api.dto.PostLoadOnHomeDto;
import cotato.timetile.domain.home.api.response.PostLoadOnHomeResponse;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.QPost;
import cotato.timetile.global.common.SortBy;
import cotato.timetile.global.common.Visibility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostHomeService {

    private static final int SIZE = 6;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional(readOnly = true)
    public PostLoadOnHomeResponse load(SortBy sortBy) {
        QPost post = QPost.post;
        BooleanExpression baseCondition = post.visibility.eq(Visibility.PUBLIC)
                .and(post.author.visibility.eq(Visibility.PUBLIC));
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy);
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .join(post.author).fetchJoin()
                .where(baseCondition)
                .orderBy(orderSpecifier)
                .limit(SIZE)
                .fetch();
        return PostLoadOnHomeResponse.of(
                posts.stream().map(PostLoadOnHomeDto::of).toList()
        );
    }

    private OrderSpecifier<?> getOrderSpecifier(SortBy sortBy) {
        QPost post = QPost.post;
        return switch (sortBy) {
            case LATEST -> post.timeInfo.createdAt.desc();
            case HOTTEST -> post.likeCount.desc();
        };
    }

}
