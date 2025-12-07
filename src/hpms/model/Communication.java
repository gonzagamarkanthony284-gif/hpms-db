package hpms.model;

import java.time.LocalDateTime;

public class Communication {
    public final String id;
    public String senderId;          // User ID (doctor or patient)
    public String recipientId;       // User ID
    public String subject;
    public String messageContent;
    public LocalDateTime sentDate;
    public LocalDateTime readDate;
    public String messageType;       // APPOINTMENT_NOTIFICATION, PRESCRIPTION_UPDATE, GENERAL, URGENT, CONSULTATION, APPOINTMENT, DISCHARGE
    public String relatedAppointmentId;
    public String relatedPrescriptionId;
    public boolean isRead = false;
    public String priority = "NORMAL"; // NORMAL, HIGH, CRITICAL
    
    public Communication(String id, String senderId, String recipientId, String subject, 
                        String messageContent, LocalDateTime sentDate) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.subject = subject;
        this.messageContent = messageContent;
        this.sentDate = sentDate;
        this.messageType = "GENERAL";
    }
    
    public boolean isFromDoctor() {
        return senderId.startsWith("S");
    }
    
    public boolean isUrgent() {
        return priority.equals("CRITICAL") || messageType.equals("URGENT");
    }
}
