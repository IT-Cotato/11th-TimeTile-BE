package cotato.timetile.domain.event.application;

import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.persistence.ArtistRepository;
import cotato.timetile.domain.event.api.dto.EventCreationDto;
import cotato.timetile.domain.event.api.request.EventCreationRequest;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.EventProcessHandler;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventCreationService {

    public static int VERIFICATION_AFTER_SECONDS = 180;
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final TaskScheduler taskScheduler;
    private final EventProcessHandler eventProcessHandler;

    @Transactional
    public void create(EventCreationRequest request, Long userId) {
        User author = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        Artist artist = artistRepository.findById(request.artistId()).orElseThrow(NotFoundException::wrong);
        Event event = Event.of(EventCreationDto.of(request, author));
        artistRepository.findAllById(request.relatedArtists()).forEach(event::addRelatedArtist);
        artist.isSubjectOf(event);
        author.write(event);
        Long eventId = eventRepository.save(event).getId();
        scheduleVerification(eventId);
    }

    private void scheduleVerification(Long eventId) {
        taskScheduler.schedule(
                () -> eventProcessHandler.removeIfWorthlessElseActivate(eventId),
                Instant.now().plusSeconds(VERIFICATION_AFTER_SECONDS)
        );
    }

}
