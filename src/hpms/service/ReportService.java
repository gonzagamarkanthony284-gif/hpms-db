package hpms.service;

import hpms.model.*;
import hpms.util.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    public static List<String> dailyAppointments() {
        LocalDate today = LocalDate.now();
        List<String> out = new ArrayList<>();
        for (Appointment a : DataStore.appointments.values())
            if (a.dateTime.toLocalDate().equals(today))
                out.add(a.id + " " + a.patientId + " " + a.staffId + " " + a.dateTime.toLocalTime());
        if (out.isEmpty())
            out.add("No appointments today");
        return out;
    }

    public static List<String> activity() {
        return new ArrayList<>(DataStore.activityLog);
    }

    public static List<String> billingOverviewDaily() {
        java.time.LocalDate today = java.time.LocalDate.now();
        List<String> out = new ArrayList<>();
        double total = 0;
        int paid = 0, pending = 0;
        for (Bill b : DataStore.bills.values()) {
            if (b.updatedAt != null && b.updatedAt.toLocalDate().equals(today)) {
                total += b.total;
                if (b.paid)
                    paid++;
                else
                    pending++;
            }
        }
        out.add("Total: " + String.format(java.util.Locale.US, "%.2f", total));
        out.add("Paid: " + paid);
        out.add("Pending: " + pending);
        return out;
    }

    public static List<String> appointmentSummary() {
        List<String> out = new ArrayList<>();
        java.util.Map<String, Long> byDept = new java.util.LinkedHashMap<>();
        for (Appointment a : DataStore.appointments.values())
            byDept.put(a.department, byDept.getOrDefault(a.department, 0L) + 1);
        for (java.util.Map.Entry<String, Long> e : byDept.entrySet())
            out.add(e.getKey() + ": " + e.getValue());
        if (out.isEmpty())
            out.add("No appointments");
        return out;
    }

    public static List<String> patientList() {
        List<String> out = new ArrayList<>();
        for (Patient p : DataStore.patients.values()) {
            if (p.isActive)
                out.add(p.id + " - " + p.name + " (" + p.age + "/" + p.gender + ")");
        }
        if (out.isEmpty())
            out.add("No active patients");
        return out;
    }

    public static List<String> medicineStockSummary() {
        return hpms.service.InventoryService.summary();
    }
}
