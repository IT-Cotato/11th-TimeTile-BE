package cotato.timetile.domain.scrap.application;

import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.domain.scrap.api.dto.ScrapStatusLoadDto;
import cotato.timetile.domain.scrap.api.request.ScrapRequest;
import cotato.timetile.domain.scrap.api.response.ScrapStatusLoadResponse;
import cotato.timetile.domain.scrap.domain.Scrap;
import cotato.timetile.domain.scrap.domain.ScrapFolder;
import cotato.timetile.domain.scrap.persistence.ScrapFolderRepository;
import cotato.timetile.domain.scrap.persistence.ScrapRepository;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final ScrapFolderRepository scrapFolderRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public ScrapStatusLoadResponse loadScrapStatus(Long postId, Long userId) {
        User scrapper = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        return ScrapStatusLoadResponse.of(
                scrapper.getScrapFolders().stream()
                        .map(scrapFolder -> ScrapStatusLoadDto.of(scrapFolder, postId))
                        .toList()
        );
    }

    @Transactional
    public void scrap(ScrapRequest request, Long postId, Long userId) {
        User scrapper = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        scrapper.getScrapFolders().forEach(scrapFolder -> {
            scrapRepository.findByPostIdAndScrapFolderId(postId, scrapFolder.getId()).ifPresent(scrap -> {
                scrap.getPost().decreaseScrapCount();
                scrapFolder.removeScrap(scrap);
            });
        });
        request.scrapFolderIds().forEach(scrapFolderId -> {
            ScrapFolder scrapFolder = scrapFolderRepository.findById(scrapFolderId)
                    .orElseThrow(NotFoundException::wrong);
            Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
            scrapFolder.addScrap(Scrap.of(scrapFolder, post));
            post.increaseScrapCount();
        });
    }

}
