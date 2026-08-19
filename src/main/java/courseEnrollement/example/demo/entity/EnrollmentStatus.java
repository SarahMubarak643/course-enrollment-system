package courseEnrollement.example.demo.entity;

import java.util.Set;

public enum EnrollmentStatus {

    ENROLLED(Set.of("APPROVED", "REJECTED", "WITHDRAWN")),
    APPROVED(Set.of("COMPLETED", "WITHDRAWN")),
    REJECTED(Set.of()),
    WITHDRAWN(Set.of()),
    COMPLETED(Set.of());

    private final Set<String> allowedNextStatuses;

    EnrollmentStatus(Set<String> allowedNextStatuses) {
        this.allowedNextStatuses = allowedNextStatuses;
    }

    public boolean canTransitionTo(String nextStatus) {
        return allowedNextStatuses.contains(nextStatus);
    }

    public Set<String> getAllowedNextStatuses() {
        return allowedNextStatuses;
    }

    public static boolean requiresReason(String status) {
        return "REJECTED".equals(status) || "WITHDRAWN".equals(status);
    }
}
