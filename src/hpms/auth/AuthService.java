package hpms.auth;

import hpms.model.UserRole;
import hpms.util.DataStore;
import hpms.util.BackupUtil;
import hpms.util.LogManager;
import hpms.util.Validators;
import java.util.*;
import java.util.Locale;
import java.security.SecureRandom;

public class AuthService {
    public static User current;
    // transient map to store last generated or reset plaintext passwords for UI display (not persisted)
    private static final Map<String,String> lastPlain = new HashMap<>();
    public static void seedAdmin() { String salt = PasswordUtil.generateSalt(); DataStore.users.put("admin", new User("admin", PasswordUtil.hash("admin123", salt), salt, UserRole.ADMIN)); }
    public static List<String> login(String username, String password) {
        List<String> out = new ArrayList<>();
        if (username == null || username.trim().isEmpty()) { out.add("Error: Missing username"); return out; }
        User u = DataStore.users.get(username);
        String h = PasswordUtil.hash(password, u==null?null:u.salt);
        if (u == null || h == null || !Objects.equals(u.password, h)) { out.add("Error: Invalid credentials"); return out; }
        current = u; LogManager.log("login " + username); out.add("Login successful"); return out;
    }
    public static List<String> logout() { List<String> out = new ArrayList<>();
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        if (current != null) { LogManager.log("logout " + current.username); current = null; }
        out.add("Logged out");
        return out;
    }
    public static List<String> register(String username, String password, String role) {
        List<String> out = new ArrayList<>();
        if (current == null || current.role != UserRole.ADMIN) { out.add("Error: Only admin can register"); return out; }
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || role == null || role.trim().isEmpty()) { out.add("Error: Missing parameters"); return out; }
        if (DataStore.users.containsKey(username)) { out.add("Error: Username exists"); return out; }
        UserRole r; try { r = UserRole.valueOf(role.toUpperCase(Locale.ROOT)); } catch (Exception e) { out.add("Error: Invalid role"); return out; }
        String salt = PasswordUtil.generateSalt();
        String hashed = PasswordUtil.hash(password, salt);
        User nu = new User(username, hashed, salt, r);
        nu.displayPassword = password;
        DataStore.users.put(username, nu);
        LogManager.log("register " + username + " " + r);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("User registered: " + username);
        return out;
    }

    // Create a patient account without requiring admin; used for patient portal accounts
    public static List<String> createPatientAccount(String username, String password) {
        List<String> out = new ArrayList<>();
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) { out.add("Error: Missing parameters"); return out; }
        if (DataStore.users.containsKey(username)) { out.add("Error: Username exists"); return out; }
        String salt = PasswordUtil.generateSalt();
        String hashed = PasswordUtil.hash(password, salt);
        User nu = new hpms.auth.User(username, hashed, salt, hpms.model.UserRole.PATIENT);
        nu.displayPassword = password;
        DataStore.users.put(username, nu);
        lastPlain.put(username, password);
        LogManager.log("register_patient " + username);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Patient account created: " + username);
        return out;
    }

    // Verify credentials without setting current user
    public static boolean verifyCredentials(String username, String password) {
        if (username == null || password == null) return false;
        User u = DataStore.users.get(username);
        if (u == null) return false;
        String h = PasswordUtil.hash(password, u.salt);
        return Objects.equals(u.password, h);
    }
    public static List<String> changePassword(String username, String oldPassword, String newPassword) {
        List<String> out = new ArrayList<>();
        if (Validators.empty(username) || Validators.empty(oldPassword) || Validators.empty(newPassword)) { out.add("Error: Missing parameters"); return out; }
        User u = DataStore.users.get(username);
        if (u == null) { out.add("Error: Unknown user"); return out; }
        String oldHash = PasswordUtil.hash(oldPassword, u.salt);
        if (!Objects.equals(oldHash, u.password)) { out.add("Error: Current password incorrect"); return out; }
        if (newPassword.length() < 6) { out.add("Error: New password too short"); return out; }
        String newSalt = PasswordUtil.generateSalt();
        String newHash = PasswordUtil.hash(newPassword, newSalt);
        u.salt = newSalt; u.password = newHash; u.displayPassword = newPassword;
        LogManager.log("change_password " + username);
        // Keep plaintext for UI display flows (transient only)
        lastPlain.put(username, newPassword);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        clearPlaintextForUI(username);
        out.add("Password changed");
        return out;
    }

    // Change a user's password without requiring the old password (used by admin or patient UI flows)
    public static List<String> changePasswordNoOld(String username, String newPassword) {
        List<String> out = new ArrayList<>();
        if (Validators.empty(username) || Validators.empty(newPassword)) { out.add("Error: Missing parameters"); return out; }
        User u = DataStore.users.get(username);
        if (u == null) { out.add("Error: Unknown user"); return out; }
        if (newPassword.length() < 6) { out.add("Error: New password too short"); return out; }
        String newSalt = PasswordUtil.generateSalt(); String newHash = PasswordUtil.hash(newPassword, newSalt); u.salt = newSalt; u.password = newHash; u.displayPassword = newPassword; LogManager.log("change_password_no_old " + username); out.add("Password changed");
        lastPlain.put(username, newPassword);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        clearPlaintextForUI(username);
        return out;
    }

    public static String resetPassword(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        User u = DataStore.users.get(username);
        if (u == null) return null;
        String pwd = generateRandomPassword(8);
        String newSalt = PasswordUtil.generateSalt();
        String newHash = PasswordUtil.hash(pwd, newSalt);
        u.salt = newSalt; u.password = newHash; u.displayPassword = pwd;
        LogManager.log("reset_password " + username);
        // keep the reset plaintext available briefly for UI display
        lastPlain.put(username, pwd);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        clearPlaintextForUI(username);
        return pwd;
    }

    // Generate a unique 6-digit code for easy entry and memorization
    public static String generateRandomPasswordForUI() {
        return generate6DigitCode();
    }

    private static String generate6DigitCode() {
        SecureRandom r = new SecureRandom();
        int code = 100000 + r.nextInt(900000); // Ensures 6 digits
        return String.valueOf(code);
    }

    private static String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom r = new SecureRandom(); StringBuilder sb = new StringBuilder(len);
        for (int i=0;i<len;i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }

    // Helper to return last-known plaintext for the username (may be null)
    public static String getLastPlaintextForUI(String username) {
        String lp = lastPlain.get(username);
        if (lp != null && !lp.isEmpty()) return lp;
        hpms.auth.User u = hpms.util.DataStore.users.get(username);
        return u == null ? null : (u.displayPassword==null || u.displayPassword.isEmpty() ? null : u.displayPassword);
    }

    // Helper to clear the stored plaintext for a user (optional privacy step)
    public static void clearPlaintextForUI(String username) { if (username != null) lastPlain.remove(username); }

    public static void migratePasswordsIfMissing() {
        boolean changed = false;
        for (hpms.auth.User u : hpms.util.DataStore.users.values()) {
            if (u == null) continue;
            if (u.role == hpms.model.UserRole.ADMIN) continue;
            if (u.password == null || u.password.isEmpty()) {
                String plain = (u.displayPassword == null || u.displayPassword.isEmpty()) ? generateRandomPasswordForUI() : u.displayPassword;
                String salt = PasswordUtil.generateSalt();
                String hash = PasswordUtil.hash(plain, salt);
                u.salt = salt;
                u.password = hash;
                if (u.displayPassword == null || u.displayPassword.isEmpty()) u.displayPassword = plain;
                lastPlain.put(u.username, plain);
                changed = true;
            }
        }
        if (changed) { try { hpms.util.BackupUtil.saveToDefault(); } catch (Exception ex) { } }
    }
}
