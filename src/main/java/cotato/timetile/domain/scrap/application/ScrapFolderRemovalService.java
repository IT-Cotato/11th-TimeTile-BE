package cotato.timetile.domain.scrap.application;

import cotato.timetile.domain.scrap.domain.ScrapFolder;
import cotato.timetile.domain.scrap.persistence.ScrapFolderRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapFolderRemovalService {

    private final ScrapFolderRepository scrapFolderRepository;

    @Transactional
    public void remove(Long scrapFolderId, Long userId) {
        ScrapFolder scrapFolder = scrapFolderRepository.findById(scrapFolderId).orElseThrow(NotFoundException::wrong);
        if (scrapFolder.isNotCreatedBy(userId)) {
            throw ForbiddenException.wrong();
        }
        scrapFolder.getCreator().removeScrapFolder(scrapFolder);
    }

}
