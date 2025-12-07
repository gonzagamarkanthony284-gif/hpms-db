package hpms.model;

import java.time.LocalDateTime;

public class StatusHistoryEntry {
    public final PatientStatus status; public final LocalDateTime at; public final String byStaffId; public final String note;
    public StatusHistoryEntry(PatientStatus status, LocalDateTime at, String byStaffId, String note){ this.status=status; this.at=at; this.byStaffId=byStaffId; this.note=note; }
}

