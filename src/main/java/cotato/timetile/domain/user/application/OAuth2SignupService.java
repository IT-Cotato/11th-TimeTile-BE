package cotato.timetile.domain.user.application;

import cotato.timetile.auth.jwt.JwtProvider;
import cotato.timetile.domain.term.domain.Term;
import cotato.timetile.domain.term.persistence.TermRepository;
import cotato.timetile.domain.user.api.dto.UserCreationDto;
import cotato.timetile.domain.user.api.request.OAuth2SignupRequest;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.domain.UserTermAgreement;
import cotato.timetile.domain.user.listener.dto.UserCreationEvent;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.BadRequestException;
import cotato.timetile.global.handler.S3Handler;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2SignupService {

    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final JwtProvider jwtProvider;
    private final S3Handler s3Handler;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void register(OAuth2SignupRequest request) {
        validateRequiredTermsAgreed(request.agreementIds());
        s3Handler.deleteNotAllowedFile(request.imageKey());
        User user = User.of(UserCreationDto.of(
                request,
                jwtProvider.parseFromTemporaryToken(request.temporaryToken()))
        );
        termRepository.findAllById(request.agreementIds())
                .forEach(term -> user.agree(UserTermAgreement.of(user, term)));
        userRepository.save(user);
        applicationEventPublisher.publishEvent(UserCreationEvent.of(user));
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
