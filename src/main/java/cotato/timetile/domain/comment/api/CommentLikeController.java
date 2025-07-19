package cotato.timetile.domain.comment.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.comment.application.CommentLikeService;
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
@RequestMapping(value = "/api/posts/{postId}/comments/{commentId}/like")
@RequiredArgsConstructor
@Tag(name = "개인 기록 댓글 좋아요")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "좋아요")
    public ResponseEntity<CommonResponse<?>> like(@PathVariable Long commentId,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentLikeService.like(commentId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @DeleteMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "좋아요 취소")
    public ResponseEntity<CommonResponse<?>> cancelLike(@PathVariable Long commentId,
                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentLikeService.cancelLike(commentId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
