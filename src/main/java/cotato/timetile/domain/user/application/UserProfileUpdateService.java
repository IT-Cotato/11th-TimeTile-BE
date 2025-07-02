package cotato.timetile.domain.user.application;

import cotato.timetile.domain.user.api.request.NicknameCheckRequest;
import cotato.timetile.domain.user.api.request.UserProfileUpdateRequest;
import cotato.timetile.domain.user.api.response.NicknameCheckResponse;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.common.S3Folder;
import cotato.timetile.global.exception.UnauthorizedException;
import cotato.timetile.global.handler.S3Handler;
import cotato.timetile.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileUpdateService {

    private final UserRepository userRepository;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public NicknameCheckResponse checkNickname(NicknameCheckRequest request) {
        return NicknameCheckResponse.of(userRepository.findByNickname(request.nickname()).isEmpty());
    }

    @Transactional
    public void updateProfile(UserProfileUpdateRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::failed);
        MultipartFile profileImage = request.profileImage();
        if (FileUtil.isAbsent(profileImage)) {
            return;
        }
        String oldImageKey = user.getImageKey();
        if (StringUtils.hasText(oldImageKey)) {
            s3Handler.deleteFile(oldImageKey);
        }
        String newImageKey = s3Handler.generateKey(profileImage, S3Folder.USER);
        s3Handler.uploadFile(profileImage, newImageKey);
        user.updateProfile(request.nickname(), request.introduction(), newImageKey);
    }

}
