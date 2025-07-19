package cotato.timetile.domain.scrap.api.response;

import cotato.timetile.domain.scrap.api.dto.ScrapStatusLoadDto;
import java.util.List;

public record ScrapStatusLoadResponse(
        List<ScrapStatusLoadDto> scrapStatus
) {
    public static ScrapStatusLoadResponse of(List<ScrapStatusLoadDto> scrapStatus) {
        return new ScrapStatusLoadResponse(scrapStatus);
    }
}
