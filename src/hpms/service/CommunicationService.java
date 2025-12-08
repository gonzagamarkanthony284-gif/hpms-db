package hpms.service;

import hpms.model.*;
import hpms.util.*;
import java.time.LocalDateTime;
import java.util.*;

public class CommunicationService {

    public static List<String> addNote(String patientId, String staffId, String text) {
        List<String> out = new ArrayList<>();
        if (!DataStore.patients.containsKey(patientId)) {
            out.add("Error: Patient not found");
            return out;
        }
        if (!DataStore.staff.containsKey(staffId)) {
            out.add("Error: Staff not found");
            return out;
        }
        DataStore.staffNotes.computeIfAbsent(patientId, k -> new ArrayList<>())
                .add(new StaffNote(staffId, text == null ? "" : text.trim(), LocalDateTime.now()));
        LogManager.log("staff_note " + patientId);
        out.add("Note added");
        return out;
    }

    public static List<StaffNote> notes(String patientId) {
        return DataStore.staffNotes.getOrDefault(patientId, new ArrayList<>());
    }

    public static List<String> addAlert(String patientId, String text) {
        List<String> out = new ArrayList<>();
        if (!DataStore.patients.containsKey(patientId)) {
            out.add("Error: Patient not found");
            return out;
        }
        DataStore.criticalAlerts.computeIfAbsent(patientId, k -> new ArrayList<>())
                .add(text == null ? "" : text.trim());
        LogManager.log("critical_alert " + patientId);
        out.add("Alert added");
        return out;
    }

    public static List<String> alerts(String patientId) {
        return DataStore.criticalAlerts.getOrDefault(patientId, new ArrayList<>());
    }

    // NEW: Messaging system for patient-doctor communication
    public static List<String> sendMessage(String senderId, String recipientId, String subject, String messageContent,
            String messageType) {
        List<String> out = new ArrayList<>();

        if (Validators.empty(senderId) || Validators.empty(recipientId) || Validators.empty(subject)
                || Validators.empty(messageContent)) {
            out.add("Error: Missing required fields");
            return out;
        }

        String id = IDGenerator.nextId("C");
        Communication comm = new Communication(id, senderId, recipientId, subject, messageContent, LocalDateTime.now());
        comm.messageType = messageType;

        DataStore.communications.put(id, comm);
        DataStore.log("send_message " + senderId + " to " + recipientId);
        out.add("Message sent successfully");
        return out;
    }

    public static List<String> getInbox(String userId) {
        List<String> out = new ArrayList<>();

        for (Communication comm : DataStore.communications.values()) {
            if (comm.recipientId.equals(userId)) {
                String status = comm.isRead ? "[Read]" : "[NEW]";
                // Get sender's name - could be staff (doctor) or patient
                String senderName = comm.senderId;
                if (comm.senderId.startsWith("S")) {
                    // Staff member (doctor, nurse, etc.)
                    Staff staff = DataStore.staff.get(comm.senderId);
                    if (staff != null && staff.name != null) {
                        senderName = staff.name;
                    }
                } else if (comm.senderId.startsWith("P")) {
                    // Patient
                    Patient patient = DataStore.patients.get(comm.senderId);
                    if (patient != null && patient.name != null) {
                        senderName = patient.name;
                    }
                }
                out.add(status + " " + comm.sentDate + " From: " + senderName + " - " + comm.subject);
            }
        }

        if (out.isEmpty())
            out.add("No messages");
        return out;
    }

    public static List<String> markAsRead(String messageId) {
        List<String> out = new ArrayList<>();

        Communication comm = DataStore.communications.get(messageId);
        if (comm == null) {
            out.add("Error: Message not found");
            return out;
        }

        comm.isRead = true;
        comm.readDate = LocalDateTime.now();
        out.add("Message marked as read");
        return out;
    }

    public static List<String> getUnreadCount(String userId) {
        List<String> out = new ArrayList<>();
        int count = 0;

        for (Communication comm : DataStore.communications.values()) {
            if (comm.recipientId.equals(userId) && !comm.isRead) {
                count++;
            }
        }

        out.add("Unread messages: " + count);
        return out;
    }
}
