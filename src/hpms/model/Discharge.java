package hpms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Discharge {
    public final String id;
    public String patientId;
    public String roomId;
    public String dischargedByDoctorId;
    public LocalDateTime dischargeDate;
    
    // Discharge information
    public String primaryDiagnosis;
    public List<String> secondaryDiagnoses = new ArrayList<>();
    public List<String> proceduresPerformed = new ArrayList<>();
    public String dischargeSummary;
    
    // Follow-up
    public String followUpInstructions;
    public LocalDateTime followUpAppointmentDate;
    public String medicationsAtDischarge;  // Current medications
    public String dietaryRestrictions;
    public String activityRestrictions;
    
    // Documentation
    public List<String> attachments = new ArrayList<>();  // File paths
    public String referredToSpecialist;
    public String referralReason;
    public final LocalDateTime createdAt;
    
    public Discharge(String id, String patientId, String roomId, String dischargedByDoctorId, 
                    LocalDateTime dischargeDate, LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.roomId = roomId;
        this.dischargedByDoctorId = dischargedByDoctorId;
        this.dischargeDate = dischargeDate;
        this.createdAt = createdAt;
    }
}
