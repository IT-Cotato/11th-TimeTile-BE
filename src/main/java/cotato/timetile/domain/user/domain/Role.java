package cotato.timetile.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    WATCHER(0, 0, 0, 0),
    LINKER(7, 3, 5, 5),
    EDITOR(14, 10, 10, 10),
    ADMIN(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int minVisitCount;
    private final int minPostCount;
    private final int minLikeCount;
    private final int minCommentCount;

    public static Role isAssigned(User user) {
        if (EDITOR.isChangeable(user)) {
            return EDITOR;
        }
        if (LINKER.isChangeable(user)) {
            return LINKER;
        }
        return WATCHER;
    }

    public static double calculateAchieveRate(User user) {
        Role nextRole = getNextRole(user.getRole());
        if (nextRole == null) {
            return 100.0;
        }
        double visitRatio = calculateRatio(user.getVisitCount(), nextRole.minVisitCount);
        double postRatio = calculateRatio(user.getPosts().size(), nextRole.minPostCount);
        double likeRatio = calculateRatio(user.getPostLikes().size(), nextRole.minLikeCount);
        double commentRatio = calculateRatio(user.getComments().size(), nextRole.minCommentCount);
        return (visitRatio + postRatio + likeRatio + commentRatio) / 4 * 100;
    }

    private boolean isChangeable(User user) {
        return user.getVisitCount() >= minVisitCount
                && user.getPosts().size() >= minPostCount
                && user.getPostLikes().size() >= minLikeCount
                && user.getComments().size() >= minCommentCount;
    }

    private static Role getNextRole(Role current) {
        return switch (current) {
            case WATCHER -> LINKER;
            case LINKER -> EDITOR;
            default -> null;
        };
    }

    private static double calculateRatio(int actual, int required) {
        return Math.min((double) actual / required, 1.0);
    }
}
