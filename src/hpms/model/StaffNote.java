package hpms.model;

import java.time.LocalDateTime;

public class StaffNote {
    public final String staffId; public final String text; public final LocalDateTime at;
    public StaffNote(String staffId, String text, LocalDateTime at){ this.staffId=staffId; this.text=text; this.at=at; }
}

