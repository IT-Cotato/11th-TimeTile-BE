package cotato.timetile.domain.scrap.application;

import cotato.timetile.domain.scrap.api.dto.ScrapFolderUpdateDto;
import cotato.timetile.domain.scrap.api.request.ScrapFolderUpdateRequest;
import cotato.timetile.domain.scrap.domain.ScrapFolder;
import cotato.timetile.domain.scrap.persistence.ScrapFolderRepository;
import cotato.timetile.global.exception.ForbiddenException;
import cotato.timetile.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapFolderUpdateService {

    private final ScrapFolderRepository scrapFolderRepository;

    @Transactional
    public void update(ScrapFolderUpdateRequest request, Long scrapFolderId, Long userId) {
        ScrapFolder scrapFolder = scrapFolderRepository.findById(scrapFolderId).orElseThrow(NotFoundException::wrong);
        if (scrapFolder.isNotCreatedBy(userId)) {
            throw ForbiddenException.wrong();
        }
        scrapFolder.update(ScrapFolderUpdateDto.of(request));
    }
    
}
