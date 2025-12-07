package hpms.model;

import java.time.LocalDateTime;

public class Prescription {
    public final String id;
    public String appointmentId;     // Link to the appointment
    public String patientId;
    public String doctorId;
    public String medicineId;        // Reference to Medicine
    public String dosage;            // e.g., "500mg"
    public String frequency;         // e.g., "Twice daily"
    public int durationDays;         // How long to take the medicine
    public String instructions;      // Additional instructions
    public String indication;        // Why prescribed (diagnosis/condition)
    public boolean isActive = true;
    public final LocalDateTime prescribedDate;
    
    public Prescription(String id, String appointmentId, String patientId, String doctorId, 
                       String medicineId, String dosage, String frequency, int durationDays, 
                       LocalDateTime prescribedDate) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.prescribedDate = prescribedDate;
    }
    
    public LocalDateTime getExpiryDate() {
        return prescribedDate.plusDays(durationDays);
    }
}
