package cotato.timetile.domain.user.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.user.api.request.NicknameCheckRequest;
import cotato.timetile.domain.user.api.request.UserProfileUpdateRequest;
import cotato.timetile.domain.user.application.UserProfileUpdateService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users/profile")
@RequiredArgsConstructor
@Tag(name = "프로필 수정")
public class UserProfileUpdateController {

    private final UserProfileUpdateService userProfileUpdateService;

    @GetMapping(value = "/nickname/check")
    @Operation(summary = "닉네임 중복 확인")
    public ResponseEntity<CommonResponse<?>> checkNickname(@Valid @ModelAttribute NicknameCheckRequest request) {
        return ApiResponseUtil.success(SuccessResponse.OK, userProfileUpdateService.checkNickname(request));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "프로필 수정")
    public ResponseEntity<CommonResponse<?>> updateProfile(@Valid @ModelAttribute UserProfileUpdateRequest request,
                                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userProfileUpdateService.updateProfile(request, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
