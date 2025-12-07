package hpms.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Data model for a staff member used by the Staff UI module.
 * This lightweight model is intentionally simple and designed for UI interaction.
 */
public class Staff {
    public String id;
    public String name;
    public StaffRole role;
    public String department;
    public String phone;
    public String email;
    public String licenseNumber;
    public String specialty;
    public String subSpecialization;
    public String nursingField;
    public Integer yearsExperience;
    public Integer yearsPractice;
    public Integer yearsOfWork;
    public String clinicSchedule_str; // String version for compatibility
    public Map<String, ScheduleEntry> clinicSchedule;
    public String qualifications;
    public String certifications;
    public String bio;
    public String employeeId;
    public String status; // Active/On Leave/Resigned
    public String photoPath;
    public boolean isAvailable;
    public LocalDateTime createdAt;

    public Staff() {
        this.createdAt = LocalDateTime.now();
        this.clinicSchedule = new HashMap<>();
        this.isAvailable = true;
    }

    public Staff(String id, String name, StaffRole role, String department, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.department = department;
        this.createdAt = createdAt;
        this.clinicSchedule = new HashMap<>();
        this.isAvailable = true;
    }

    public static class ScheduleEntry {
        public boolean active;
        public String startTime;
        public String endTime;

        public ScheduleEntry(boolean active, String startTime, String endTime) {
            this.active = active;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    @Override
    public String toString() {
        return String.format("Staff[id=%s,name=%s,role=%s,dept=%s]", id, name, role, department);
    }
}
