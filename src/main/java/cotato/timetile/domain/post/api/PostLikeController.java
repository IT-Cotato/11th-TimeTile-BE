package cotato.timetile.domain.post.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.post.application.PostLikeService;
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
@RequestMapping(value = "/api/posts/{postId}/like")
@RequiredArgsConstructor
@Tag(name = "개인 기록 좋아요")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "좋아요")
    public ResponseEntity<CommonResponse<?>> like(@PathVariable Long postId,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postLikeService.like(postId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @DeleteMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "좋아요 취소")
    public ResponseEntity<CommonResponse<?>> cancelLike(@PathVariable Long postId,
                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postLikeService.cancelLike(postId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
