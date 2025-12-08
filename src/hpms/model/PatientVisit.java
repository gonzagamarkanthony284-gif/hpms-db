package hpms.model;

import java.time.LocalDateTime;

/**
 * Represents a single patient visit/arrival record.
 * Each time a patient arrives or receives a new diagnosis, a NEW PatientVisit
 * record is created.
 * The original arrival data remains unchanged in the Patient object.
 */
public class PatientVisit {
    public final int id;
    public final String patientId;
    public final LocalDateTime visitDate;
    public String registrationType; // Walk-in, Emergency, Referral, etc.
    public String incidentTime; // Time of incident for emergency/trauma cases
    public String broughtBy; // Ambulance, Family, Police, etc.
    public String initialBp; // Initial blood pressure reading
    public String initialHr; // Initial heart rate
    public String initialSpo2; // Initial SpO2 reading
    public String chiefComplaint; // Main complaint/reason for visit
    public String attendingDoctor; // Staff ID of attending doctor
    public String diagnosis; // Diagnosis for this visit
    public String treatmentPlan; // Treatment plan for this visit
    public String notes; // Additional notes
    public String visitStatus; // Active, Completed, Follow-up Required, etc.

    public PatientVisit(int id, String patientId, LocalDateTime visitDate, String registrationType) {
        this.id = id;
        this.patientId = patientId;
        this.visitDate = visitDate;
        this.registrationType = registrationType;
        this.visitStatus = "Active";
    }

    /**
     * Constructor for creating a new visit (before database insertion)
     */
    public PatientVisit(String patientId, String registrationType) {
        this.id = -1; // Will be set by database auto-increment
        this.patientId = patientId;
        this.visitDate = LocalDateTime.now();
        this.registrationType = registrationType;
        this.visitStatus = "Active";
    }

    @Override
    public String toString() {
        return "Visit #" + id + " - " + registrationType + " on " + visitDate +
                (diagnosis != null ? " - " + diagnosis : "");
    }
}
