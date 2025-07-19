package cotato.timetile.domain.event.application;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.event.api.dto.ContributorDto;
import cotato.timetile.domain.event.api.dto.EventLoadHotDto;
import cotato.timetile.domain.event.api.dto.EventLoadMoreDto;
import cotato.timetile.domain.event.api.dto.EventLoadPendingDto;
import cotato.timetile.domain.event.api.dto.RelatedArtistDto;
import cotato.timetile.domain.event.api.dto.RelatedEventDto;
import cotato.timetile.domain.event.api.response.ContributorResponse;
import cotato.timetile.domain.event.api.response.EventLoadHotAndActiveYearResponse;
import cotato.timetile.domain.event.api.response.EventLoadHotResponse;
import cotato.timetile.domain.event.api.response.EventLoadMoreResponse;
import cotato.timetile.domain.event.api.response.EventLoadPendingResponse;
import cotato.timetile.domain.event.api.response.EventLoadResponse;
import cotato.timetile.domain.event.domain.ChangeType;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.QEvent;
import cotato.timetile.domain.event.domain.RelatedArtist;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.handler.S3Handler;
import cotato.timetile.global.util.DiffMatchPatcher;
import cotato.timetile.global.util.DiffMatchPatcher.Diff;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventLoadService {

    private static final int PAGE_SIZE = 5;
    private static final int SLICE_SIZE = 5;
    private static final int HOVER_SIZE = 3;
    private final EventRepository eventRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final S3Handler s3Handler;
    private final DiffMatchPatcher diffMatchPatcher;

    @Transactional(readOnly = true)
    public EventLoadHotAndActiveYearResponse loadHotAndActiveYear(String artistId) {
        QEvent e1 = QEvent.event;
        QEvent e2 = new QEvent("e2");
        BooleanExpression baseCondition = e1.artist.id.eq(artistId)
                .and(e1.active.isTrue())
                .and(e1.id.eq(
                        JPAExpressions
                                .select(e2.id.max())
                                .from(e2)
                                .where(
                                        e2.groupId.eq(e1.groupId),
                                        e2.artist.id.eq(artistId),
                                        e2.active.isTrue()
                                )
                ));
        List<Event> fetch = jpaQueryFactory
                .selectFrom(e1)
                .where(baseCondition)
                .orderBy(e1.postCount.desc(), e1.id.desc())
                .fetch();
        return EventLoadHotAndActiveYearResponse.of(
                fetch.stream()
                        .collect(Collectors.groupingBy(
                                        event -> event.getStartedAt().getYear(),
                                        (Supplier<Map<Integer, List<String>>>) () -> new TreeMap<>(Comparator.reverseOrder()),
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                list -> list.stream()
                                                        .sorted(Comparator.comparing(Event::getPostCount).reversed())
                                                        .limit(HOVER_SIZE)
                                                        .map(Event::getName)
                                                        .toList()
                                        )
                                )
                        )

        );
    }

    @Transactional(readOnly = true)
    public EventLoadHotResponse loadHotOnYear(String artistId, int year) {
        return EventLoadHotResponse.of(
                eventRepository.findAllByArtistIdAndActiveIsTrueAndYear(artistId, year).stream()
                        .sorted(Comparator.comparing(Event::getPostCount).reversed())
                        .limit(2)
                        .map(event -> EventLoadHotDto.of(
                                        event,
                                        getRelatedEvents(event.getRelatedEvents())
                                )
                        )
                        .collect(Collectors.groupingBy(event -> event.startedAt().getMonthValue()))
        );
    }

    @Transactional(readOnly = true)
    public EventLoadMoreResponse loadMore(String artistId, int year, int month, Long lastEventId) {
        QEvent e1 = QEvent.event;
        QEvent e2 = new QEvent("e2");
        BooleanExpression baseCondition = e1.startedAt.year().eq(year)
                .and(e1.startedAt.month().eq(month))
                .and(e1.artist.id.eq(artistId))
                .and(e1.active.isTrue())
                .and(e1.id.eq(
                        JPAExpressions
                                .select(e2.id.max())
                                .from(e2)
                                .where(
                                        e2.groupId.eq(e1.groupId),
                                        e2.artist.id.eq(artistId),
                                        e2.active.isTrue()
                                )
                ));
        BooleanExpression cursorCondition = e1.id.lt(lastEventId);

        List<Event> fetched = jpaQueryFactory
                .selectFrom(e1)
                .where(baseCondition, cursorCondition)
                .orderBy(e1.postCount.desc(), e1.id.desc())
                .limit(SLICE_SIZE + 1)
                .fetch();
        boolean hasNext = fetched.size() > SLICE_SIZE;
        List<Event> events = hasNext ? fetched.subList(0, SLICE_SIZE) : fetched;
        Optional<Event> lastEventOpt = events.isEmpty() ? Optional.empty() : Optional.of(events.get(events.size() - 1));
        return EventLoadMoreResponse.of(
                events.stream()
                        .map(event -> EventLoadMoreDto.of(event, getRelatedEvents(event.getRelatedEvents())))
                        .toList(),
                hasNext,
                lastEventOpt.map(Event::getId).orElse(null)
        );
    }

    @Transactional(readOnly = true)
    public EventLoadResponse loadActive(String groupId) {
        Event event = eventRepository.findTopByGroupIdAndActiveIsTrueOrderByIdDesc(groupId)
                .orElseThrow(NotFoundException::wrong);
        return EventLoadResponse.of(
                event,
                getRelatedEvents(event.getRelatedEvents())
        );
    }

    @Transactional(readOnly = true)
    public ContributorResponse loadParticipants(String groupId) {
        return ContributorResponse.of(
                eventRepository.findAllByGroupIdAndActiveIsTrueOrderByEditedAtDesc(groupId).stream()
                        .map(event -> {
                                    User participant = event.getAuthor();
                                    return ContributorDto.of(
                                            participant,
                                            s3Handler.getSimpleLogoUrlIfNull(participant.getImageKey())
                                    );
                                }
                        )
                        .distinct()
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public EventLoadPendingResponse loadPending(ChangeType changeType, int page) {
        return EventLoadPendingResponse.of(
                eventRepository.findAllByActiveIsFalseOrderByEditedAtDesc(changeType,
                                PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Direction.DESC, "editedAt"))
                        )
                        .getContent()
                        .stream()
                        .map(pendingEvent -> eventRepository.findTopByGroupIdAndActiveIsTrueOrderByIdDesc(
                                        pendingEvent.getGroupId()).map(
                                        displayedEvent -> getDifference(pendingEvent, displayedEvent, ChangeType.EDITED))
                                .orElseGet(() -> getDifference(pendingEvent, pendingEvent, ChangeType.NEW))
                        ).toList()
        );
    }

    private EventLoadPendingDto getDifference(Event pendingEvent, Event displayedEvent, ChangeType changeType) {
        return EventLoadPendingDto.of(
                pendingEvent.getId(),
                getStringDifference(displayedEvent.getName(), pendingEvent.getName()),
                getStringDifference(displayedEvent.getDescription(), pendingEvent.getDescription()),
                getStringDifference(displayedEvent.getSource(), pendingEvent.getSource()),
                getStringDifference(displayedEvent.getStartedAt().toString(), pendingEvent.getStartedAt().toString()),
                getStringDifference(displayedEvent.getEndedAt().toString(), pendingEvent.getEndedAt().toString()),
                getSetDifference(
                        pendingEvent.getRelatedArtists().stream().map(RelatedArtist::getArtist)
                                .map(RelatedArtistDto::of).toList(),
                        displayedEvent.getRelatedArtists().stream().map(RelatedArtist::getArtist)
                                .map(RelatedArtistDto::of).toList()
                ).stream().toList(),
                getSetDifference(
                        displayedEvent.getRelatedArtists().stream().map(RelatedArtist::getArtist)
                                .map(RelatedArtistDto::of).toList(),
                        pendingEvent.getRelatedArtists().stream().map(RelatedArtist::getArtist)
                                .map(RelatedArtistDto::of).toList()
                ).stream().toList(),
                getRelatedEvents(
                        getSetDifference(pendingEvent.getRelatedEvents(), displayedEvent.getRelatedEvents()).stream()
                                .toList()),
                getRelatedEvents(
                        getSetDifference(displayedEvent.getRelatedEvents(), pendingEvent.getRelatedEvents()).stream()
                                .toList()),
                getSetDifference(pendingEvent.getActivityTypes(), displayedEvent.getActivityTypes()).stream().toList(),
                getSetDifference(displayedEvent.getActivityTypes(), pendingEvent.getActivityTypes()).stream().toList(),
                getSetDifference(pendingEvent.getRelatedMaterials(), displayedEvent.getRelatedMaterials()).stream()
                        .toList(),
                getSetDifference(displayedEvent.getRelatedMaterials(), pendingEvent.getRelatedMaterials()).stream()
                        .toList(),
                pendingEvent.getEditedAt(),
                changeType
        );
    }

    private LinkedList<Diff> getStringDifference(String original, String modified) {
        LinkedList<Diff> stringDifferences = diffMatchPatcher.diff_main(original, modified);
        diffMatchPatcher.diff_cleanupSemantic(stringDifferences);
        return stringDifferences;
    }

    private <T> Set<T> getSetDifference(Collection<T> a, Collection<T> b) {
        Set<T> setDifference = new LinkedHashSet<>(a);
        setDifference.removeAll(b);
        return setDifference;
    }

    private List<RelatedEventDto> getRelatedEvents(List<String> relatedEvents) {
        return relatedEvents.stream()
                .map(eventRepository::findTopByGroupIdAndActiveIsTrueOrderByIdDesc)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(RelatedEventDto::of)
                .toList();
    }

}
