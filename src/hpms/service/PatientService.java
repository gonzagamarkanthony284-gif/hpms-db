package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.time.LocalDateTime;
import java.util.*;

public class PatientService {
    public static List<String> add(String name, String age, String gender, String contact, String address) {
        List<String> out = new ArrayList<>();
        if (Validators.empty(name) || Validators.empty(age) || Validators.empty(gender) || Validators.empty(contact) || Validators.empty(address)) { out.add("Error: Missing parameters"); return out; }
        Gender g;
        try {
            String gu = gender == null ? "" : gender.trim();
            String genderUpper = gu.toUpperCase(Locale.ROOT);
            // Normalize common forms and map to enum safely
            if ("MALE".equalsIgnoreCase(gu) || "M".equalsIgnoreCase(gu)) g = Gender.Male;
            else if ("FEMALE".equalsIgnoreCase(gu) || "F".equalsIgnoreCase(gu)) g = Gender.Female;
            else if ("LGBTQ+".equalsIgnoreCase(gu) || "LGBTQ_PLUS".equalsIgnoreCase(genderUpper) || "LGBTQ".equalsIgnoreCase(genderUpper)) g = Gender.LGBTQ_PLUS;
            else g = Gender.OTHER; // default for 'Other', 'Prefer not to say' and unknown values
        } catch (Exception e) { out.add("Error: Invalid gender"); return out; }
        int a; try { a = Integer.parseInt(age.trim()); } catch (Exception e) { out.add("Error: Invalid age"); return out; }
        if (a <= 0) { out.add("Error: Invalid age"); return out; }
        for (Patient p : DataStore.patients.values()) if (p.contact.equalsIgnoreCase(contact.trim())) { out.add("Error: Duplicate contact"); return out; }
        String id = IDGenerator.nextId("P"); Patient p = new Patient(id, name.trim(), a, g, contact.trim(), address.trim(), LocalDateTime.now());
        DataStore.patients.put(id, p); LogManager.log("add_patient " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Patient created " + id);
        return out;
    }
    // extended add allowing optional patient-provided and insurance fields
    public static List<String> add(String name, String age, String gender, String contact, String address,
                                   String registrationType, String incidentTime, String broughtBy,
                                   String initialBp, String initialHr, String initialSpo2, String chiefComplaint,
                                   String allergies, String medications, String pastMedicalHistory,
                                   String smokingStatus, String alcoholUse, String occupation,
                                   String insuranceProvider, String insuranceId, String policyHolderName,
                                   String policyHolderDob, String policyRelationship) {
        List<String> out = add(name, age, gender, contact, address);
        if (out.isEmpty()) { out.add("Error: unknown failure"); return out; }
        // if creation succeeded, parse id from returned message "Patient created <id>"
        String created = out.get(0);
        if (!created.startsWith("Patient created ")) return out;
        String id = created.split(" ")[2];
        Patient p = DataStore.patients.get(id);
        if (p == null) { out.clear(); out.add("Error: creation failed"); return out; }
        // registration / arrival metadata
        if (registrationType != null && !registrationType.trim().isEmpty()) p.registrationType = registrationType.trim();
        if (p.registrationType == null || p.registrationType.trim().isEmpty()) p.registrationType = "Walk-in Patient";
        if (incidentTime != null) p.incidentTime = incidentTime.trim();
        if (broughtBy != null) p.broughtBy = broughtBy.trim();
        if (initialBp != null) p.initialBp = initialBp.trim();
        if (initialHr != null) p.initialHr = initialHr.trim();
        if (initialSpo2 != null) p.initialSpo2 = initialSpo2.trim();
        if (chiefComplaint != null) p.chiefComplaint = chiefComplaint.trim();
        if (allergies != null) p.allergies = allergies.trim();
        if (medications != null) p.medications = medications.trim();
        if (pastMedicalHistory != null) p.pastMedicalHistory = pastMedicalHistory.trim();
        if (smokingStatus != null) p.smokingStatus = smokingStatus.trim();
        if (alcoholUse != null) p.alcoholUse = alcoholUse.trim();
        if (occupation != null) p.occupation = occupation.trim();
        if (insuranceProvider != null) p.insuranceProvider = insuranceProvider.trim();
        if (insuranceId != null) p.insuranceId = insuranceId.trim();
        if (policyHolderName != null) p.policyHolderName = policyHolderName.trim();
        if (policyHolderDob != null) p.policyHolderDob = policyHolderDob.trim();
        if (policyRelationship != null) p.policyRelationship = policyRelationship.trim();
        LogManager.log("add_patient_extended " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.clear(); out.add("Patient created " + id);
        return out;
    }

    // extended edit to update patient-provided and insurance fields
    public static List<String> editExtended(String id, String name, String age, String gender, String contact, String address,
                                            String registrationType, String incidentTime, String broughtBy,
                                            String initialBp, String initialHr, String initialSpo2, String chiefComplaint,
                                            String allergies, String medications, String pastMedicalHistory,
                                            String smokingStatus, String alcoholUse, String occupation,
                                            String insuranceProvider, String insuranceId, String policyHolderName,
                                            String policyHolderDob, String policyRelationship) {
        List<String> out = edit(id, name, age, gender, contact, address);
        if (out.isEmpty()) { out.add("Error: unknown failure"); return out; }
        Patient p = DataStore.patients.get(id);
        if (p == null) { out.clear(); out.add("Error: Invalid patient ID"); return out; }
        // registration / arrival metadata
        if (registrationType != null && !registrationType.trim().isEmpty()) p.registrationType = registrationType.trim();
        if (p.registrationType == null || p.registrationType.trim().isEmpty()) p.registrationType = "Walk-in Patient";
        if (incidentTime != null) p.incidentTime = incidentTime.trim();
        if (broughtBy != null) p.broughtBy = broughtBy.trim();
        if (initialBp != null) p.initialBp = initialBp.trim();
        if (initialHr != null) p.initialHr = initialHr.trim();
        if (initialSpo2 != null) p.initialSpo2 = initialSpo2.trim();
        if (chiefComplaint != null) p.chiefComplaint = chiefComplaint.trim();
        if (allergies != null) p.allergies = allergies.trim();
        if (medications != null) p.medications = medications.trim();
        if (pastMedicalHistory != null) p.pastMedicalHistory = pastMedicalHistory.trim();
        if (smokingStatus != null) p.smokingStatus = smokingStatus.trim();
        if (alcoholUse != null) p.alcoholUse = alcoholUse.trim();
        if (occupation != null) p.occupation = occupation.trim();
        if (insuranceProvider != null) p.insuranceProvider = insuranceProvider.trim();
        if (insuranceId != null) p.insuranceId = insuranceId.trim();
        if (policyHolderName != null) p.policyHolderName = policyHolderName.trim();
        if (policyHolderDob != null) p.policyHolderDob = policyHolderDob.trim();
        if (policyRelationship != null) p.policyRelationship = policyRelationship.trim();
        LogManager.log("edit_patient_extended " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.clear(); out.add("Patient updated " + id);
        return out;
    }

    public static void migrateRegistrationTypeDefault() {
        boolean changed = false;
        for (Patient p : DataStore.patients.values()) {
            if (p == null) continue;
            if (p.registrationType == null || p.registrationType.trim().isEmpty()) {
                p.registrationType = "Walk-in Patient";
                changed = true;
            }
        }
        if (changed) { try { BackupUtil.saveToDefault(); } catch (Exception ex) { } }
    }

    // staff (doctor/nurse) adds clinical info: vitals and a progress note
    public interface ClinicalUpdateListener { void clinicalUpdated(String patientId); }
    private static final java.util.List<ClinicalUpdateListener> clinicalListeners = new java.util.ArrayList<>();
    public static void addClinicalUpdateListener(ClinicalUpdateListener l) { if (l!=null) clinicalListeners.add(l); }
    public static void removeClinicalUpdateListener(ClinicalUpdateListener l) { clinicalListeners.remove(l); }

    private static void notifyClinicalUpdate(String patientId) { for (ClinicalUpdateListener l : clinicalListeners) { try { l.clinicalUpdated(patientId); } catch (Exception ex) { /* ignore listener error */ } } }

    public static List<String> addClinicalInfo(String id, String heightCmStr, String weightKgStr, String bloodPressure, String note, String byStaffId,
                                               String xrayPath, String xrayStatus, String xraySummary,
                                               String stoolPath, String stoolStatus, String stoolSummary,
                                               String urinePath, String urineStatus, String urineSummary,
                                               String bloodPath, String bloodStatus, String bloodSummary,
                                               java.util.List<String> otherAttachments) {
        List<String> out = new ArrayList<>(); Patient p = DataStore.patients.get(id);
        if (p == null) { out.add("Error: Invalid patient ID"); return out; }
        try { if (heightCmStr != null && !heightCmStr.trim().isEmpty()) p.heightCm = Double.parseDouble(heightCmStr.trim()); } catch (Exception e) { out.add("Error: invalid height"); return out; }
        try { if (weightKgStr != null && !weightKgStr.trim().isEmpty()) p.weightKg = Double.parseDouble(weightKgStr.trim()); } catch (Exception e) { out.add("Error: invalid weight"); return out; }
        if (bloodPressure != null) p.bloodPressure = bloodPressure.trim();
        if (note != null && !note.trim().isEmpty()) p.progressNotes.add(java.time.LocalDateTime.now() + " by " + (byStaffId==null?"unknown":byStaffId) + ": " + note.trim());
        // store uploaded test paths and summaries (optional)
        if (xrayPath != null) p.xrayFilePath = xrayPath;
        if (xrayStatus != null) p.xrayStatus = xrayStatus;
        if (xraySummary != null) p.xraySummary = xraySummary;

        if (stoolPath != null) p.stoolFilePath = stoolPath;
        if (stoolStatus != null) p.stoolStatus = stoolStatus;
        if (stoolSummary != null) p.stoolSummary = stoolSummary;

        if (urinePath != null) p.urineFilePath = urinePath;
        if (urineStatus != null) p.urineStatus = urineStatus;
        if (urineSummary != null) p.urineSummary = urineSummary;

        if (bloodPath != null) p.bloodFilePath = bloodPath;
        if (bloodStatus != null) p.bloodStatus = bloodStatus;
        if (bloodSummary != null) p.bloodSummary = bloodSummary;
        // add any additional attachments to the patient's attachments list
        if (otherAttachments != null && !otherAttachments.isEmpty()) {
            for (String a : otherAttachments) if (a != null && !a.trim().isEmpty()) p.attachmentPaths.add(a.trim());
        }
        LogManager.log("add_clinical " + id + " by " + (byStaffId==null?"?":byStaffId));
        // notify listeners so dashboards and other UI can refresh
        notifyClinicalUpdate(id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Clinical info updated " + id);
        return out;
    }
    public static List<String> edit(String id, String name, String age, String gender, String contact, String address) {
        List<String> out = new ArrayList<>(); Patient p = DataStore.patients.get(id);
        if (p == null) { out.add("Error: Invalid patient ID"); return out; }
        if (Validators.empty(name) || Validators.empty(age) || Validators.empty(gender) || Validators.empty(contact) || Validators.empty(address)) { out.add("Error: Missing parameters"); return out; }
        Gender g;
        try {
            String gu = gender == null ? "" : gender.trim();
            String genderUpper = gu.toUpperCase(Locale.ROOT);
            if ("MALE".equalsIgnoreCase(gu) || "M".equalsIgnoreCase(gu)) g = Gender.Male;
            else if ("FEMALE".equalsIgnoreCase(gu) || "F".equalsIgnoreCase(gu)) g = Gender.Female;
            else if ("LGBTQ+".equalsIgnoreCase(gu) || "LGBTQ_PLUS".equalsIgnoreCase(genderUpper) || "LGBTQ".equalsIgnoreCase(genderUpper)) g = Gender.LGBTQ_PLUS;
            else g = Gender.OTHER;
        } catch (Exception e) { out.add("Error: Invalid gender"); return out; }
        int a; try { a = Integer.parseInt(age.trim()); } catch (Exception e) { out.add("Error: Invalid age"); return out; }
        if (a <= 0) { out.add("Error: Invalid age"); return out; }
        for (Patient other : DataStore.patients.values()) if (!other.id.equals(id) && other.contact.equalsIgnoreCase(contact.trim())) { out.add("Error: Duplicate contact"); return out; }
        p.name = name.trim(); p.age = a; p.gender = g; p.contact = contact.trim(); p.address = address.trim(); LogManager.log("edit_patient " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Patient updated " + id);
        return out;
    }
    public static List<String> delete(String id) { List<String> out = new ArrayList<>(); Patient p = DataStore.patients.remove(id); if (p == null) { out.add("Error: Invalid patient ID"); return out; } for (Room r : DataStore.rooms.values()) if (id.equals(r.occupantPatientId)) { r.status = RoomStatus.VACANT; r.occupantPatientId = null; } LogManager.log("delete_patient " + id); try { BackupUtil.saveToDefault(); } catch (Exception ex) { } out.add("Patient deleted " + id); return out; }
    public static List<String> search(String id) { List<String> out = new ArrayList<>(); Patient p = DataStore.patients.get(id); if (p == null) { out.add("Error: Invalid patient ID"); return out; } out.add(p.id + " " + p.name + " " + p.age + " " + p.gender); return out; }
}
