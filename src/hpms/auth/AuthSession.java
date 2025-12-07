package hpms.auth;

import hpms.model.UserRole;

/**
 * Session object to carry authenticated user information across the application
 */
public class AuthSession {
    public final String userId;
    public final String username;
    public final String fullName;
    public final UserRole role;
    public final String department;

    public AuthSession(String userId, String username, String fullName, UserRole role, String department) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.department = department;
    }

    public boolean isDoctor() {
        return role == UserRole.DOCTOR;
    }

    public boolean isNurse() {
        return role == UserRole.NURSE;
    }

    public boolean isPatient() {
        return role == UserRole.PATIENT;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}
