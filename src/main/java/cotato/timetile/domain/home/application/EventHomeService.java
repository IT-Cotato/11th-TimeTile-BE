package cotato.timetile.domain.home.application;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.QEvent;
import cotato.timetile.domain.home.api.dto.EventLoadOnHomeDto;
import cotato.timetile.domain.home.api.response.EventLoadOnHomeResponse;
import cotato.timetile.global.common.SortBy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventHomeService {

    private static final int SIZE = 6;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional(readOnly = true)
    public EventLoadOnHomeResponse load(SortBy sortBy) {
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");
        BooleanExpression baseCondition = e1.active.isTrue()
                .and(e1.id.eq(
                        JPAExpressions
                                .select(e2.id.max())
                                .from(e2)
                                .where(
                                        e2.groupId.eq(e1.groupId),
                                        e2.active.isTrue()
                                )
                ));
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy);
        List<Event> events = jpaQueryFactory
                .selectFrom(e1)
                .where(baseCondition)
                .orderBy(orderSpecifier)
                .limit(SIZE)
                .fetch();
        return EventLoadOnHomeResponse.of(
                events.stream().map(EventLoadOnHomeDto::of).toList()
        );
    }

    private OrderSpecifier<?> getOrderSpecifier(SortBy sortBy) {
        QEvent event = QEvent.event;
        return switch (sortBy) {
            case LATEST -> event.editedAt.desc();
            case HOTTEST -> event.postCount.desc();
        };
    }

}
