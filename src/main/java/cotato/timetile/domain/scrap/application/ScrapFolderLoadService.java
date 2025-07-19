package cotato.timetile.domain.scrap.application;

import cotato.timetile.domain.scrap.api.dto.ScrapFolderLoadAllDto;
import cotato.timetile.domain.scrap.api.response.ScrapFolderLoadAllResponse;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapFolderLoadService {

    private final UserRepository userRepository;

    public ScrapFolderLoadAllResponse loadAll(Long userId) {
        return ScrapFolderLoadAllResponse.of(
                userRepository.findById(userId).orElseThrow(UnauthorizedException::failed).getScrapFolders().stream()
                        .map(ScrapFolderLoadAllDto::of)
                        .toList()
        );
    }

}
