package cotato.timetile.domain.artist.application;

import cotato.timetile.domain.artist.api.response.ArtistFollowStatusResponse;
import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.artist.domain.ArtistFollow;
import cotato.timetile.domain.artist.persistence.ArtistFollowRepository;
import cotato.timetile.domain.artist.persistence.ArtistRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.ConflictException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistFollowService {

    private final ArtistRepository artistRepository;
    private final ArtistFollowRepository artistFollowRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(String artistId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        boolean alreadyFollowed = user.getArtistFollows().stream()
                .anyMatch(af -> af.getArtist().getId().equals(artistId));
        if (alreadyFollowed) {
            throw ConflictException.wrong();
        }
        Artist artist = artistRepository.findById(artistId).orElseThrow(NotFoundException::wrong);
        user.follow(ArtistFollow.of(user, artist));
        artist.increaseFollowCount();
    }

    @Transactional
    public void unfollow(String artistId, Long userId) {
        ArtistFollow artistFollow = artistFollowRepository.findByArtistIdAndUserId(artistId, userId)
                .orElseThrow(NotFoundException::wrong);
        artistFollow.getUser().unfollow(artistFollow);
        artistFollow.getArtist().decreaseFollowCount();
    }

    @Transactional(readOnly = true)
    public ArtistFollowStatusResponse loadFollowStatus(String artistId, Long userId) {
        return ArtistFollowStatusResponse.of(
                artistFollowRepository.findByArtistIdAndUserId(artistId, userId).isPresent()
        );
    }

}
