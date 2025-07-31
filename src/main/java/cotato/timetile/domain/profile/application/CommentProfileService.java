package cotato.timetile.domain.profile.application;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.comment.domain.QComment;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.QEvent;
import cotato.timetile.domain.profile.api.dto.CommentLoadAllOnProfileDto;
import cotato.timetile.domain.profile.api.response.CommentLoadAllOnProfileResponse;
import cotato.timetile.global.helper.QuerydslHelper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentProfileService {

    private static final int COMMENT_PAGE_SIZE = 18;
    private final QuerydslHelper querydslHelper;

    @Transactional(readOnly = true)
    public CommentLoadAllOnProfileResponse loadMyComments(int page, Long userId) {
        QComment c = QComment.comment;
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        PageRequest pageable = PageRequest.of(page - 1, COMMENT_PAGE_SIZE);

        BooleanExpression condition = c.commenter.id.eq(userId);

        List<Tuple> tuples = querydslHelper.joinLatestActiveEvent(e1, e2, c, c.post.groupId)
                .where(condition)
                .orderBy(c.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(c, c.id, condition);

        return CommentLoadAllOnProfileResponse.of(
                tuples.stream()
                        .map(tuple -> {
                            Comment comment = Objects.requireNonNull(tuple.get(c));
                            Event event = Objects.requireNonNull(tuple.get(e1));
                            return CommentLoadAllOnProfileDto.of(
                                    comment,
                                    event
                            );
                        }).toList(),
                new PageImpl<>(tuples, pageable, total)
        );
    }

}
