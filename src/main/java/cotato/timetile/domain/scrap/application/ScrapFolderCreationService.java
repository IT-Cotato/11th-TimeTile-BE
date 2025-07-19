package cotato.timetile.domain.scrap.application;

import cotato.timetile.domain.scrap.api.dto.ScrapFolderCreationDto;
import cotato.timetile.domain.scrap.api.request.ScrapFolderCreationRequest;
import cotato.timetile.domain.scrap.domain.ScrapFolder;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapFolderCreationService {

    private final UserRepository userRepository;

    @Transactional
    public void create(ScrapFolderCreationRequest request, Long userId) {
        User creator = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        creator.createScrapFolder(ScrapFolder.of(ScrapFolderCreationDto.of(request, creator)));
    }

}
