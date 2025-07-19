package cotato.timetile.domain.user.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.user.application.UserFollowService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users/{followerId}/follow")
@RequiredArgsConstructor
@Tag(name = "유저")
public class UserFollowController {

    private final UserFollowService userFollowService;

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "팔로우")
    public ResponseEntity<CommonResponse<?>> follow(@PathVariable Long followerId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userFollowService.follow(followerId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @DeleteMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "언팔로우")
    public ResponseEntity<CommonResponse<?>> unfollow(@PathVariable Long followerId,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userFollowService.unfollow(followerId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }
}
