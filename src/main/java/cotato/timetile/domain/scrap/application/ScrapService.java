package cotato.timetile.domain.scrap.application;

import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.domain.scrap.api.dto.ScrapStatusLoadDto;
import cotato.timetile.domain.scrap.api.request.ScrapRequest;
import cotato.timetile.domain.scrap.api.response.ScrapStatusLoadResponse;
import cotato.timetile.domain.scrap.domain.Scrap;
import cotato.timetile.domain.scrap.domain.ScrapFolder;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

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
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
        List<ScrapFolder> scrapFolders = scrapper.getScrapFolders();
        validateScrapFolders(scrapFolders, request.scrapFolderIds());
        updateScraps(scrapFolders, post, request.scrapFolderIds());
    }

    private void validateScrapFolders(List<ScrapFolder> scrapFolders, List<Long> requestedFolderIds) {
        if (!new HashSet<>(scrapFolders.stream().map(ScrapFolder::getId).toList())
                .containsAll(requestedFolderIds)) {
            throw ForbiddenException.wrong();
        }
    }

    private void updateScraps(List<ScrapFolder> scrapFolders, Post post, List<Long> targetFolderIds) {
        scrapFolders.forEach(scrapFolder -> {
            if (targetFolderIds.contains(scrapFolder.getId()) && !scrapFolder.alreadyScrapped(post.getId())) {
                scrapFolder.addScrap(Scrap.of(scrapFolder, post));
                post.increaseScrapCount();
            } else if (!targetFolderIds.contains(scrapFolder.getId()) && scrapFolder.alreadyScrapped(post.getId())) {
                scrapFolder.getScraps().removeIf(scrap -> scrap.getPost().equals(post));
                post.decreaseScrapCount();
            }
        });
    }

}
