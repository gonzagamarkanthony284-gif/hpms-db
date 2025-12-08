package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.time.LocalDateTime;
import java.util.*;

public class StaffService {
    public static List<String> add(String name, String role, String department) {
        return add(name, role, department, null, null, null, null, null, null);
    }

    public static List<String> add(String name, String role, String department, String specialization, String phone, String email, String licenseNumber, String qualifications, String notes) {
        List<String> out = new ArrayList<>();
        if (Validators.empty(name) || Validators.empty(role) || Validators.empty(department)) { out.add("Error: Missing parameters"); return out; }
        StaffRole r; try { r = StaffRole.valueOf(role.toUpperCase(Locale.ROOT)); } catch (Exception e) { out.add("Error: Invalid role"); return out; }
        if (!DataStore.departments.contains(department.trim())) { out.add("Error: Invalid department"); return out; }

        // Basic input checks
        if (name.trim().length() > 120) { out.add("Error: Name too long"); return out; }
        if (!Validators.empty(email) && !isValidEmail(email)) { out.add("Error: Invalid email address"); return out; }
        if (!Validators.empty(phone) && !isValidPhone(phone)) { out.add("Error: Invalid phone number"); return out; }

        // Prevent obvious duplicates by name+role+department
        for (Staff existing : DataStore.staff.values()) {
            if (existing.name != null && existing.name.equalsIgnoreCase(name.trim()) && existing.role == r && existing.department != null && existing.department.equalsIgnoreCase(department.trim())) {
                out.add("Error: Staff already exists with same name, role and department");
                return out;
            }
            // If licenseNumber is provided and matches an existing license -> reject
            if (!Validators.empty(licenseNumber) && licenseNumber.equalsIgnoreCase(existing.licenseNumber)) {
                out.add("Error: A staff member with the same license number already exists");
                return out;
            }
            // If email provided and matches existing -> reject
            if (!Validators.empty(email) && email.equalsIgnoreCase(existing.email)) {
                out.add("Error: A staff member with the same email already exists");
                return out;
            }
        }

        String id = IDGenerator.nextId("S");
        Staff s = new Staff(id, name.trim(), r, department.trim(), LocalDateTime.now());
        if (!Validators.empty(specialization)) s.specialty = specialization.trim();
        if (!Validators.empty(phone)) s.phone = phone.trim();
        if (!Validators.empty(email)) s.email = email.trim();
        if (!Validators.empty(licenseNumber)) s.licenseNumber = licenseNumber.trim();
        if (!Validators.empty(qualifications)) s.qualifications = qualifications.trim();
        if (!Validators.empty(notes)) {
            // reuse qualifications field as notes fallback if qualifications empty
            s.qualifications = (s.qualifications==null?"":s.qualifications) + (s.qualifications.isEmpty()?"":" | ") + notes.trim();
        }

        DataStore.staff.put(id, s);
        LogManager.log("add_staff " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Staff added " + id);
        return out;
    }

    private static boolean isValidEmail(String email) {
        if (email == null) return false;
        // Simple, permissive pattern — sufficient for basic validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String p = phone.trim();
        // Accept digits, space, +, -, parentheses and between 7 and 25 characters
        return p.matches("^[0-9+()\\\\\\-\\s]{7,25}$");
    }
    public static List<String> delete(String id) {
        List<String> out = new ArrayList<>();
        Staff s = DataStore.staff.remove(id);
        if (s == null) { out.add("Error: Invalid staff ID"); return out; }

        int affected = 0;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (Appointment a : DataStore.appointments.values()) {
            if (id.equals(a.staffId) && !a.isCompleted && a.dateTime.isAfter(now.minusHours(1))) {
                affected++;
                a.notes = (a.notes==null?"":"[Doctor unavailable] ") + "Doctor deleted; please reschedule";
                try { CommunicationService.addAlert(a.patientId, "Your doctor (" + (s.name==null?id:s.name) + ") is no longer available. Please choose another doctor."); } catch (Exception ex) { }
            }
        }

        LogManager.log("delete_staff " + id + " affected_appts=" + affected);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Staff deleted " + id + (affected>0? (" — notified patients for " + affected + " appointment(s)") : ""));
        return out;
    }
}
