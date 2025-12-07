package hpms.model;

import java.time.LocalDateTime;

public class LabResult {
    public final String id; public final String testRequestId; public final String resultText; public final LocalDateTime enteredAt;
    public LabResult(String id, String testRequestId, String resultText, LocalDateTime enteredAt) { this.id=id; this.testRequestId=testRequestId; this.resultText=resultText; this.enteredAt=enteredAt; }
}

