package cotato.timetile.domain.profile.application;

import cotato.timetile.domain.profile.api.request.UserProfileUpdateRequest;
import cotato.timetile.domain.profile.api.response.UserGradeResponse;
import cotato.timetile.domain.profile.api.response.UserProfileLoadResponse;
import cotato.timetile.domain.user.api.response.NicknameCheckResponse;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.listener.dto.UserUpdateEvent;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.S3Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final S3Handler s3Handler;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public NicknameCheckResponse checkNickname(String nickname) {
        return NicknameCheckResponse.of(userRepository.findByNickname(nickname).isEmpty());
    }

    @Transactional
    public void update(UserProfileUpdateRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        s3Handler.deleteNotAllowedFile(request.imageKey());
        s3Handler.deleteFile(user.getImageKey());
        user.updateProfile(request.nickname(), request.introduction(), request.imageKey());
        applicationEventPublisher.publishEvent(UserUpdateEvent.of(user));
    }

    @Transactional
    public void checkAndChangeUserRole() {
        List<User> users = userRepository.findAll();
        users.forEach(User::updateRole);
    }

    @Transactional(readOnly = true)
    public UserProfileLoadResponse loadProfile(Long targetId) {
        User user = userRepository.findById(targetId).orElseThrow(NotFoundException::wrong);
        String profileImageUrl = s3Handler.getSimpleLogoUrlIfNull(user.getImageKey());
        int postCount = user.getPosts().size();
        return UserProfileLoadResponse.of(user, profileImageUrl, postCount);
    }

    @Transactional(readOnly = true)
    public UserGradeResponse loadGrade(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        return UserGradeResponse.of(user);
    }

}
