package cotato.timetile.domain.user.application;

import cotato.timetile.auth.mail.MailSender;
import cotato.timetile.domain.term.domain.Term;
import cotato.timetile.domain.term.persistence.TermRepository;
import cotato.timetile.domain.user.api.dto.UserCreationDto;
import cotato.timetile.domain.user.api.request.EmailCheckRequest;
import cotato.timetile.domain.user.api.request.EmailVerificationRequest;
import cotato.timetile.domain.user.api.request.LocalSignupRequest;
import cotato.timetile.domain.user.api.response.EmailCheckResponse;
import cotato.timetile.domain.user.api.response.EmailVerificationResponse;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.domain.UserTermAgreement;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.BadRequestException;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.RedisHandler;
import cotato.timetile.global.handler.S3Handler;
import cotato.timetile.global.util.VerificationCodeGenerator;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocalSignupService {

    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final MailSender mailSender;
    private final RedisHandler redisHandler;
    private final S3Handler s3Handler;
    private static final int CODE_VALID_DURATION = 3;
    private static final int VERIFIED_VALID_DURATION = 30;

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
        boolean isValid = redisHandler.get(request.email()) != null &&
                redisHandler.get(request.email()).toString().equals(request.verificationCode());
        if (isValid) {
            redisHandler.set(request.email(), "true", Duration.ofMinutes(VERIFIED_VALID_DURATION));
        }
        return EmailVerificationResponse.of(isValid);
    }

    @Transactional
    public void register(LocalSignupRequest request) {
        boolean isVerified = redisHandler.get(request.email()) != null &&
                redisHandler.get(request.email()).equals("true");
        if (!isVerified) {
            throw UnauthorizedException.unverified();
        }
        validateRequiredTermsAgreed(request.agreementIds());
        s3Handler.deleteNotAllowedFile(request.imageKey());
        User user = User.of(UserCreationDto.of(request));
        termRepository.findAllById(request.agreementIds())
                .forEach(term -> user.agree(UserTermAgreement.of(user, term)));
        userRepository.save(user);
    }

    private void validateRequiredTermsAgreed(List<Long> agreementIds) {
        List<Long> requiredTermIds = termRepository.findLatestTerms().stream()
                .filter(Term::isRequired)
                .map(Term::getId)
                .toList();

        if (!new HashSet<>(agreementIds).containsAll(requiredTermIds)) {
            throw BadRequestException.wrong();
        }
    }

}
