package cotato.timetile.domain.user.application;

import cotato.timetile.auth.mail.MailSender;
import cotato.timetile.domain.user.api.request.EmailCheckRequest;
import cotato.timetile.domain.user.api.request.EmailVerificationRequest;
import cotato.timetile.domain.user.api.request.LocalSignupRequest;
import cotato.timetile.domain.user.api.response.EmailCheckResponse;
import cotato.timetile.domain.user.api.response.EmailVerificationResponse;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.handler.RedisHandler;
import cotato.timetile.global.util.VerificationCodeGenerator;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocalSignupService {

    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final RedisHandler redisHandler;
    private static final int CODE_VALID_DURATION = 3;

    @Transactional(readOnly = true)
    public EmailCheckResponse checkEmail(EmailCheckRequest request) {
        return EmailCheckResponse.of(userRepository.findByEmail(request.email()).isEmpty());
    }

    public void sendCode(EmailCheckRequest request) {
        String verificationCode = VerificationCodeGenerator.generateVerificationCode();
        mailSender.sendMail(request.email(), verificationCode);
        redisHandler.set(request.email(), verificationCode, Duration.ofMinutes(CODE_VALID_DURATION));
    }

    public EmailVerificationResponse verifyEmail(EmailVerificationRequest request) {
        return EmailVerificationResponse.of(
                redisHandler.get(request.email()).toString().equals(request.verificationCode()));
    }

    @Transactional
    public void register(LocalSignupRequest request) {
        userRepository.save(request.toUser());
    }

}
