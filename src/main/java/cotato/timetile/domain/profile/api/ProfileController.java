package cotato.timetile.domain.profile.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.profile.api.request.UserProfileUpdateRequest;
import cotato.timetile.domain.profile.application.CommentProfileService;
import cotato.timetile.domain.profile.application.EventProfileService;
import cotato.timetile.domain.profile.application.FollowProfileService;
import cotato.timetile.domain.profile.application.PostProfileService;
import cotato.timetile.domain.profile.application.UserProfileService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.NumberParam;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.common.Visibility;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@Tag(name = "프로필")
public class ProfileController {

    private final FollowProfileService followProfileService;
    private final PostProfileService postProfileService;
    private final CommentProfileService commentProfileService;
    private final UserProfileService userProfileService;
    private final EventProfileService eventProfileService;

    @GetMapping(value = "/profile/nickname/check")
    @Operation(summary = "닉네임 중복 확인")
    public ResponseEntity<CommonResponse<?>> checkNickname(@Valid @RequestParam String nickname) {
        return ApiResponseUtil.success(SuccessResponse.OK, userProfileService.checkNickname(nickname));
    }

    @PutMapping(value = "/me/profile")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "수정")
    public ResponseEntity<CommonResponse<?>> update(@Valid @RequestBody UserProfileUpdateRequest request,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userProfileService.update(request, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @GetMapping(value = "/me/profile")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내 프로필 조회")
    public ResponseEntity<CommonResponse<?>> loadProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK, userProfileService.loadMyProfile(userPrincipal.getId()));
    }

    @GetMapping(value = "/{targetId}/profile")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "다른 유저 프로필 조회")
    public ResponseEntity<CommonResponse<?>> loadProfile(@PathVariable Long targetId,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                userProfileService.loadOtherUserProfile(targetId, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/grade")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "등급 조회")
    public ResponseEntity<CommonResponse<?>> loadGrade(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                userProfileService.loadGrade(userPrincipal.getId()));
    }

    @GetMapping(value = "/me/follower-users")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내 팔로워 유저 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowerUsers(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                               @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastFollowId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followProfileService.loadFollowerUsers(userPrincipal.getId(), lastFollowId));
    }

    @GetMapping(value = "/me/following-users")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내 팔로잉 유저 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowingUsers(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastFollowId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followProfileService.loadFollowingUsers(userPrincipal.getId(), lastFollowId));
    }

    @GetMapping(value = "/me/following-artists")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내 팔로잉 아티스트 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowingArtists(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                  @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastFollowId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followProfileService.loadFollowingArtists(userPrincipal.getId(), lastFollowId));
    }

    @GetMapping(value = "/{targetId}/follower-users")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "다른 유저의 팔로워 유저 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowers(@PathVariable Long targetId,
                                                           @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastFollowId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followProfileService.loadFollowerUsers(targetId, lastFollowId));
    }

    @GetMapping(value = "/{targetId}/following-users")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "다른 유저의 팔로잉 유저 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowingUsers(@PathVariable Long targetId,
                                                                @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastFollowId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followProfileService.loadFollowingUsers(targetId, lastFollowId));
    }

    @GetMapping(value = "/{targetId}/following-artists")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "다른 유저의 팔로잉 아티스트 조회")
    public ResponseEntity<CommonResponse<?>> loadFollowingArtists(@PathVariable Long targetId,
                                                                  @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastFollowId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                followProfileService.loadFollowingArtists(targetId, lastFollowId));
    }

    @GetMapping(value = "/me/profile/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "프로필에 표시되는 내 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadPostsOnMyProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                postProfileService.loadPostsOnMyProfile(userPrincipal.getId()));
    }

    @GetMapping(value = "/{targetId}/profile/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "프로필에 표시되는 다른 유저 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadPostsOnOtherUserProfile(@PathVariable Long targetId,
                                                                         @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastPostId,
                                                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                postProfileService.loadPostsOnOtherUserProfile(targetId, lastPostId, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/likes/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "좋아요한 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadLikedPosts(
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                postProfileService.loadLikedPosts(page, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/scraps/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "스크랩한 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadScrappedPosts(
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                postProfileService.loadScrappedPosts(page, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/scraps/{scrapFolderId}/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "스크랩 폴더 내 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadMyScrappedPosts(
            @PathVariable Long scrapFolderId,
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                postProfileService.loadScrappedPostsInFolder(scrapFolderId, page, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/posts")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내가 작성한 개별 기록 조회")
    public ResponseEntity<CommonResponse<?>> loadMyPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "PUBLIC") Visibility visibility,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                postProfileService.loadMyPosts(page, visibility, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/comments")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내가 작성한 댓글 조회")
    public ResponseEntity<CommonResponse<?>> loadMyComments(
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                commentProfileService.loadMyComments(page, userPrincipal.getId()));
    }

    @GetMapping(value = "/me/events/artists")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내가 작성한 이벤트의 아티스트 조회")
    public ResponseEntity<CommonResponse<?>> loadMyEventArtists(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                eventProfileService.loadMyEventArtists(userPrincipal.getId()));
    }

    @GetMapping(value = "/me/events/artists/{artistId}")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "내가 작성한 이벤트 조회")
    public ResponseEntity<CommonResponse<?>> loadMyEventArtists(@PathVariable String artistId,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                eventProfileService.loadMyEvents(artistId, page, userPrincipal.getId()));
    }

}
