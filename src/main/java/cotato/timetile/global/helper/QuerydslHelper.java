package cotato.timetile.global.helper;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.event.domain.QEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuerydslHelper {
    private final JPAQueryFactory jpaQueryFactory;

    public <P> JPAQuery<Tuple> joinLatestActiveEvent(QEvent e1, QEvent e2, EntityPathBase<P> parent,
                                                     StringPath groupIdPath) {
        return jpaQueryFactory.select(parent, e1)
                .from(parent)
                .innerJoin(e1).on(
                        groupIdPath.eq(e1.groupId)
                                .and(e1.id.eq(
                                        JPAExpressions.select(e2.id.max())
                                                .from(e2)
                                                .where(e2.groupId.eq(groupIdPath)
                                                        .and(e2.active.isTrue()))
                                ))
                );
    }

    public <P> Long getTotalCount(EntityPathBase<P> entity, NumberPath<Long> idPath, BooleanExpression condition) {
        return jpaQueryFactory
                .select(idPath.count())
                .from(entity)
                .where(condition)
                .fetchOne();
    }

}