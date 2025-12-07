package hpms.auth;

/**
 * Role-based access control guard
 */
public class RoleGuard {
    private static AuthSession currentSession = null;

    public static void setSession(AuthSession session) {
        currentSession = session;
    }

    public static AuthSession getSession() {
        return currentSession;
    }

    public static void clearSession() {
        currentSession = null;
    }

    public static boolean hasSession() {
        return currentSession != null;
    }

    public static boolean isDoctor() {
        return hasSession() && currentSession.isDoctor();
    }

    public static boolean isNurse() {
        return hasSession() && currentSession.isNurse();
    }

    public static boolean isPatient() {
        return hasSession() && currentSession.isPatient();
    }

    public static boolean isAdmin() {
        return hasSession() && currentSession.isAdmin();
    }

    public static void requireDoctor() throws AccessDeniedException {
        if (!isDoctor()) {
            throw new AccessDeniedException("Doctor access required");
        }
    }

    public static void requireAdmin() throws AccessDeniedException {
        if (!isAdmin()) {
            throw new AccessDeniedException("Admin access required");
        }
    }

    public static void requireAuthenticated() throws AccessDeniedException {
        if (!hasSession()) {
            throw new AccessDeniedException("Authentication required");
        }
    }

    public static class AccessDeniedException extends Exception {
        public AccessDeniedException(String message) {
            super(message);
        }
    }
}
