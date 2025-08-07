package cotato.timetile.domain.search.api.response;

import cotato.timetile.domain.search.api.dto.MainSearchUserDto;
import cotato.timetile.domain.user.domain.UserDocument;
import java.util.List;
import org.springframework.data.domain.Page;

public record MainSearchUserResponse(
        List<MainSearchUserDto> users,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious,
        boolean isLast
) {
    public static MainSearchUserResponse of(List<MainSearchUserDto> users, Page<UserDocument> userPage) {
        return new MainSearchUserResponse(
                users,
                userPage.getNumber() + 1,
                userPage.getSize(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.hasNext(),
                userPage.hasPrevious(),
                userPage.isLast()
        );
    }
}
