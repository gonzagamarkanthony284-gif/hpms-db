package hpms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    public final String id;
    public final String patientId;
    public final List<BillItem> items = new ArrayList<>();
    public double total;
    public boolean paid;
    public PaymentMethod paymentMethod;
    public final LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Bill(String id, String patientId, double initial, LocalDateTime createdAt) {
        this.id = id; this.patientId = patientId; this.total = initial; this.createdAt = createdAt; this.updatedAt = createdAt;
    }
}

