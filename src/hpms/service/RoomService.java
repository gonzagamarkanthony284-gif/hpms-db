package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.util.*;

public class RoomService {
    public static List<String> assign(String roomId, String patientId) {
        List<String> out = new ArrayList<>();
        Room r = DataStore.rooms.get(roomId);
        Patient p = DataStore.patients.get(patientId);
        if (r == null) {
            out.add("Error: Assigning room that does not exist");
            return out;
        }
        if (p == null) {
            out.add("Error: Invalid patient ID");
            return out;
        }
        PatientStatus status = PatientStatusService.getStatus(patientId);
        if (status != PatientStatus.INPATIENT) {
            // Enhanced error message with current status
            String message = "Error: Only inpatients can be assigned to rooms. Current status: " + status;
            out.add(message);
            // Audit log the failed attempt
            LogManager.log("assign_room_rejected patient=" + patientId + " room=" + roomId
                    + " reason=wrong_status current_status=" + status);
            return out;
        }
        if (r.status == RoomStatus.OCCUPIED) {
            out.add("Error: Room is occupied");
            LogManager.log("assign_room_rejected patient=" + patientId + " room=" + roomId
                    + " reason=room_occupied");
            return out;
        }
        r.status = RoomStatus.OCCUPIED;
        r.occupantPatientId = patientId;
        // Enhanced audit log with more details
        LogManager.log("assign_room room=" + roomId + " patient=" + patientId
                + " patient_name=" + (p != null ? p.name : "unknown")
                + " status=" + status);
        try {
            BackupUtil.saveToDefault();
        } catch (Exception ex) {
        }
        out.add("Room assigned");
        return out;
    }

    public static List<String> vacate(String roomId) {
        List<String> out = new ArrayList<>();
        Room r = DataStore.rooms.get(roomId);
        if (r == null) {
            out.add("Error: Room does not exist");
            return out;
        }
        String previousOccupant = r.occupantPatientId;
        r.status = RoomStatus.VACANT;
        r.occupantPatientId = null;
        // Enhanced audit log
        LogManager.log("vacate_room room=" + roomId
                + " previous_occupant=" + (previousOccupant != null ? previousOccupant : "none"));
        try {
            BackupUtil.saveToDefault();
        } catch (Exception ex) {
        }
        out.add("Room vacated");
        return out;
    }
}
