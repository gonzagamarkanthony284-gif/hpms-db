package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientStatusService {
    public static List<String> setStatus(String patientId, String status, String byStaffId, String note){
        List<String> out = new ArrayList<>(); Patient p = DataStore.patients.get(patientId); if (p==null){ out.add("Error: Patient not found"); return out; }
        PatientStatus st; try { st = PatientStatus.valueOf(status.toUpperCase(java.util.Locale.ROOT)); } catch (Exception e){ out.add("Error: Invalid status"); return out; }
        DataStore.patientStatus.put(patientId, st);
        DataStore.statusHistory.computeIfAbsent(patientId, k->new ArrayList<>()).add(new StatusHistoryEntry(st, LocalDateTime.now(), byStaffId, note));
        LogManager.log("status_change "+patientId+" "+st);
        out.add("Status updated to "+st);
        return out;
    }
    public static PatientStatus getStatus(String patientId){ return DataStore.patientStatus.getOrDefault(patientId, PatientStatus.OUTPATIENT); }
    public static List<StatusHistoryEntry> history(String patientId){ return DataStore.statusHistory.getOrDefault(patientId, new ArrayList<>()); }
}

