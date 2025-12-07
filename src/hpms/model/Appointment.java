package hpms.model;

import java.time.LocalDateTime;

public class Appointment {
    public final String id;
    public String patientId;
    public String staffId;
    public LocalDateTime dateTime;
    public String department;
    public ConsultationType consultationType = ConsultationType.FOLLOW_UP;
    public String notes = "";                          // Patient notes/chief complaint
    public String diagnosis = "";                      // Doctor's diagnosis
    public boolean isCompleted = false;
    public String outcome = "";                        // Successful, Referred, etc.
    public final LocalDateTime createdAt;
    
    public Appointment(String id, String patientId, String staffId, LocalDateTime dateTime, String department, LocalDateTime createdAt) {
        this.id = id; 
        this.patientId = patientId; 
        this.staffId = staffId; 
        this.dateTime = dateTime; 
        this.department = department; 
        this.createdAt = createdAt;
    }
}

