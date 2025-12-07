package hpms.model;

import java.time.LocalDateTime;

public class Patient {
    public final String id;
    public String name;
    public int age;
    public Gender gender;
    public String contact;
    public String address;
    // Patient-provided / administrative fields
    public String allergies = ""; // free text or comma-separated
    public String medications = ""; // free text
    public String pastMedicalHistory = "";
    public String surgicalHistory = "";
    public String familyHistory = "";

    // Lifestyle
    public String smokingStatus = ""; // Current/Former/Never
    public String alcoholUse = "";
    public String drugUse = "";
    public String occupation = "";

    // uploaded files (paths) for records
    public java.util.List<String> attachmentPaths = new java.util.ArrayList<>();

    // Clinical / hospital-entered
    public Double heightCm = null;
    public Double weightKg = null;
    public String bloodPressure = "";
    // Registration / arrival metadata
    public String registrationType = ""; // Mode of arrival / type of registration
    public String incidentTime = "";    // if emergency / accident - time of incident (HH:mm or ISO)
    public String broughtBy = "";       // e.g., Ambulance;Family;Bystander;Police
    public String initialBp = "";       // e.g., "120/80"
    public String initialHr = "";       // heart rate
    public String initialSpo2 = "";     // SpO2 percent
    public String chiefComplaint = "";  // initial complaint text
    public java.util.List<String> progressNotes = new java.util.ArrayList<>();
    public java.util.List<String> labResults = new java.util.ArrayList<>();
    public java.util.List<String> radiologyReports = new java.util.ArrayList<>();
    // Clinical test uploads and summaries
    public String xrayFilePath = null; // path to uploaded x-ray image
    public String xrayStatus = null; // e.g., Not Uploaded, Uploaded, Reviewed, Critical
    public String xraySummary = null; // short summary / findings

    public String stoolFilePath = null;
    public String stoolStatus = null;
    public String stoolSummary = null;

    public String urineFilePath = null;
    public String urineStatus = null;
    public String urineSummary = null;

    public String bloodFilePath = null;
    public String bloodStatus = null;
    public String bloodSummary = null;
    public java.util.List<String> diagnoses = new java.util.ArrayList<>();
    public java.util.List<String> treatmentPlans = new java.util.ArrayList<>();
    public java.util.List<String> dischargeSummaries = new java.util.ArrayList<>();

    // Insurance / billing
    public String insuranceProvider = "";
    public String insuranceId = "";
    public String insuranceGroup = "";
    public String policyHolderName = "";
    public String policyHolderDob = "";
    public String policyRelationship = "";
    public String secondaryInsurance = "";
    public final LocalDateTime createdAt;
    public Patient(String id, String name, int age, Gender gender, String contact, String address, LocalDateTime createdAt) {
        this.id = id; this.name = name; this.age = age; this.gender = gender; this.contact = contact; this.address = address; this.createdAt = createdAt;
        // Note: patient portal passwords are managed by AuthService and stored securely in DataStore.users
    }

    /**
     * Compute BMI from stored height (cm) and weight (kg).
     * Returns null if height or weight isn't set.
     * BMI is rounded to two decimal places.
     */
    public Double getBmi() {
        if (heightCm == null || weightKg == null) return null;
        if (heightCm == 0) return null;
        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);
        // round to two decimals
        return Math.round(bmi * 100.0) / 100.0;
    }
}

