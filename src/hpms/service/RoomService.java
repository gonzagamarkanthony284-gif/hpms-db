package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.util.*;

public class RoomService {
    public static List<String> assign(String roomId, String patientId) {
        List<String> out = new ArrayList<>(); Room r = DataStore.rooms.get(roomId); Patient p = DataStore.patients.get(patientId);
        if (r == null) { out.add("Error: Assigning room that does not exist"); return out; }
        if (p == null) { out.add("Error: Invalid patient ID"); return out; }
        if (r.status == RoomStatus.OCCUPIED) { out.add("Error: Room is occupied"); return out; }
        r.status = RoomStatus.OCCUPIED; r.occupantPatientId = patientId; LogManager.log("assign_room " + roomId + " " + patientId);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Room assigned"); return out;
    }
    public static List<String> vacate(String roomId) { List<String> out = new ArrayList<>(); Room r = DataStore.rooms.get(roomId); if (r == null) { out.add("Error: Room does not exist"); return out; } r.status = RoomStatus.VACANT; r.occupantPatientId = null; LogManager.log("vacate_room " + roomId); try { BackupUtil.saveToDefault(); } catch (Exception ex) { } out.add("Room vacated"); return out; }
}
