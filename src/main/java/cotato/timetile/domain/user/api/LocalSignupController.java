package cotato.timetile.domain.user.api;

import cotato.timetile.domain.user.api.request.EmailCheckRequest;
import cotato.timetile.domain.user.api.request.EmailVerificationRequest;
import cotato.timetile.domain.user.api.request.LocalSignupRequest;
import cotato.timetile.domain.user.application.LocalSignupService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Tag(name = "로컬 회원 가입")
public class LocalSignupController {

    private final LocalSignupService localSignupService;

    @GetMapping(value = "/email/check")
    @Operation(summary = "이메일 중복 확인")
    public ResponseEntity<CommonResponse<?>> checkEmail(@Valid @Email String email) {
        return ApiResponseUtil.success(SuccessResponse.OK, localSignupService.checkEmail(email));
    }

    @PostMapping(value = "/email/send-code")
    @Operation(summary = "이메일 인증 번호 전송")
    public ResponseEntity<CommonResponse<?>> sendCode(@Valid @RequestBody EmailCheckRequest request) {
        localSignupService.sendCode(request);
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @PostMapping(value = "/email/verify")
    @Operation(summary = "이메일 인증 확인")
    public ResponseEntity<CommonResponse<?>> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
        return ApiResponseUtil.success(SuccessResponse.OK, localSignupService.verifyEmail(request));
    }

    @PostMapping("/signup")
    @Operation(summary = "회원 가입")
    public ResponseEntity<CommonResponse<?>> signup(@Valid @RequestBody LocalSignupRequest request) {
        localSignupService.register(request);
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

}
