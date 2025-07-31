package cotato.timetile.domain.user.scheduler;

import cotato.timetile.domain.profile.application.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleScheduler {

    private final UserProfileService userProfileService;

    @Scheduled(cron = "0 0 3 1 * *")
    public void checkAndChangeUserRole() {
        userProfileService.checkAndChangeUserRole();
    }

}
