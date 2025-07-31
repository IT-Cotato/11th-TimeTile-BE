package cotato.timetile.domain.profile.application;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.domain.QArtist;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.QEvent;
import cotato.timetile.domain.profile.api.dto.ArtistFilterDto;
import cotato.timetile.domain.profile.api.dto.EventLoadAllOnProfileDto;
import cotato.timetile.domain.profile.api.response.ArtistFilterResponse;
import cotato.timetile.domain.profile.api.response.EventLoadAllOnPageResponse;
import cotato.timetile.global.helper.QuerydslHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventProfileService {

    private static final int EVENT_PAGE_SIZE = 5;
    private final JPAQueryFactory jpaQueryFactory;
    private final QuerydslHelper querydslHelper;

    @Transactional(readOnly = true)
    public ArtistFilterResponse loadMyEventArtists(Long userId) {
        QEvent event = QEvent.event;
        QArtist artist = QArtist.artist;
        List<Artist> fetched = jpaQueryFactory
                .selectFrom(artist)
                .join(artist.events, event)
                .where(event.author.id.eq(userId))
                .orderBy(artist.id.desc())
                .fetch();
        return ArtistFilterResponse.of(
                fetched.stream()
                        .map(ArtistFilterDto::of)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public EventLoadAllOnPageResponse loadMyEvents(String artistId, int page, Long userId) {
        QEvent e1 = QEvent.event, e2 = new QEvent("e2");

        PageRequest pageable = PageRequest.of(page - 1, EVENT_PAGE_SIZE);

        BooleanExpression condition = e1.author.id.eq(userId)
                .and(e1.artist.id.eq(artistId))
                .and(e1.id.eq(
                                JPAExpressions
                                        .select(e2.id.max())
                                        .from(e2)
                                        .where(
                                                e2.author.id.eq(userId),
                                                e2.artist.id.eq(artistId),
                                                e2.groupId.eq(e1.groupId)
                                        )
                        )
                );

        List<Event> events = jpaQueryFactory
                .selectFrom(e1)
                .where(condition)
                .orderBy(e1.startedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = querydslHelper.getTotalCount(e1, e1.id, condition);

        return EventLoadAllOnPageResponse.of(
                events.stream()
                        .map(EventLoadAllOnProfileDto::of)
                        .toList(),
                new PageImpl<>(events, pageable, total)
        );
    }

}
