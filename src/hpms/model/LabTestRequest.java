package hpms.model;

import java.time.LocalDateTime;

public class LabTestRequest {
    public final String id; public final String patientId; public final String doctorId; public final String testName; public final LocalDateTime requestedAt;
    public LabTestRequest(String id, String patientId, String doctorId, String testName, LocalDateTime requestedAt) { this.id=id; this.patientId=patientId; this.doctorId=doctorId; this.testName=testName; this.requestedAt=requestedAt; }
}

