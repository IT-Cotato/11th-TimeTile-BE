package cotato.timetile.domain.event.api.response;

import cotato.timetile.domain.event.api.dto.ContributorDto;
import java.util.List;

public record ContributorResponse(
        List<ContributorDto> contributors
) {
    public static ContributorResponse of(List<ContributorDto> contributors) {
        return new ContributorResponse(contributors);
    }
}
