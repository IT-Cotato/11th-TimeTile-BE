package cotato.timetile.domain.comment.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.comment.api.request.CommentCreationRequest;
import cotato.timetile.domain.comment.api.request.CommentUpdateRequest;
import cotato.timetile.domain.comment.application.CommentCreationService;
import cotato.timetile.domain.comment.application.CommentLoadService;
import cotato.timetile.domain.comment.application.CommentRemovalService;
import cotato.timetile.domain.comment.application.CommentUpdateService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "댓글")
public class CommentController {

    private final CommentLoadService commentLoadService;
    private final CommentCreationService commentCreationService;
    private final CommentUpdateService commentUpdateService;
    private final CommentRemovalService commentRemovalService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "전체 조회")
    public ResponseEntity<CommonResponse<?>> loadAll(@PathVariable Long postId) {
        return ApiResponseUtil.success(SuccessResponse.OK, commentLoadService.loadAll(postId));
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "생성")
    public ResponseEntity<CommonResponse<?>> create(@Valid @RequestBody CommentCreationRequest request,
                                                    @PathVariable Long postId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentCreationService.create(request, postId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @PutMapping(value = "/{commentId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "수정")
    public ResponseEntity<CommonResponse<?>> update(@Valid @RequestBody CommentUpdateRequest request,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentUpdateService.update(request, commentId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @DeleteMapping(value = "/{commentId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "삭제")
    public ResponseEntity<CommonResponse<?>> remove(@PathVariable Long commentId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentRemovalService.remove(commentId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
