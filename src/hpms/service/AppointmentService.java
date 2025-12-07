package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.time.*;
import java.util.*;

public class AppointmentService {
    public static List<String> schedule(String patientId, String staffId, String date, String time, String department) {
        List<String> out = new ArrayList<>(); if (Validators.empty(patientId) || Validators.empty(staffId) || Validators.empty(date) || Validators.empty(time) || Validators.empty(department)) { out.add("Error: Missing parameters"); return out; }
        Patient p = DataStore.patients.get(patientId); Staff staffObj = DataStore.staff.get(staffId);
        if (p == null) { out.add("Error: Invalid patient ID"); return out; }
        if (staffObj == null) { out.add("Error: Invalid staff ID"); return out; }
        if (!DataStore.departments.contains(department.trim())) { out.add("Error: Invalid department"); return out; }
        LocalDate d; LocalTime t; try { d = LocalDate.parse(date.trim()); } catch (Exception e) { out.add("Error: Incorrect date"); return out; } try { t = LocalTime.parse(time.trim()); } catch (Exception e) { out.add("Error: Incorrect time"); return out; }
        LocalDateTime dt = LocalDateTime.of(d, t); if (dt.isBefore(LocalDateTime.now())) { out.add("Error: Scheduling in the past"); return out; }
        // assume appointments have 1-hour duration; prevent overlapping
        java.time.LocalDateTime proposedStart = dt; java.time.LocalDateTime proposedEnd = dt.plusHours(1);
        for (Appointment a : DataStore.appointments.values()) if (a.staffId.equals(staffId)) {
            java.time.LocalDateTime start = a.dateTime; java.time.LocalDateTime end = a.dateTime.plusHours(1);
            if (!(proposedEnd.isBefore(start) || proposedStart.isAfter(end) || proposedEnd.equals(start) || proposedStart.equals(end))) { out.add("Error: Staff double-booked (overlap)"); return out; }
        }
        String id = IDGenerator.nextId("A"); Appointment a = new Appointment(id, patientId, staffId, dt, department.trim(), LocalDateTime.now()); DataStore.appointments.put(id, a); LogManager.log("schedule_appt " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Appointment created " + id);
        return out;
    }
    public static List<String> cancel(String id) { List<String> out = new ArrayList<>(); Appointment a = DataStore.appointments.remove(id); if (a == null) { out.add("Error: Invalid appointment ID"); return out; } LogManager.log("cancel_appt " + id); try { BackupUtil.saveToDefault(); } catch (Exception ex) { } out.add("Appointment canceled " + id); return out; }
    public static List<String> reschedule(String id, String date, String time) {
        List<String> out = new ArrayList<>(); Appointment a = DataStore.appointments.get(id); if (a == null) { out.add("Error: Invalid appointment ID"); return out; }
        LocalDate d; LocalTime t; try { d = LocalDate.parse(date.trim()); } catch (Exception e) { out.add("Error: Incorrect date"); return out; } try { t = LocalTime.parse(time.trim()); } catch (Exception e) { out.add("Error: Incorrect time"); return out; }
        LocalDateTime dt = LocalDateTime.of(d, t); if (dt.isBefore(LocalDateTime.now())) { out.add("Error: Scheduling in the past"); return out; }
        // check overlap assuming 1-hour duration
        java.time.LocalDateTime proposedStart = dt; java.time.LocalDateTime proposedEnd = dt.plusHours(1);
        for (Appointment x : DataStore.appointments.values()) if (!x.id.equals(id) && x.staffId.equals(a.staffId)) {
            java.time.LocalDateTime s = x.dateTime; java.time.LocalDateTime e = x.dateTime.plusHours(1);
            if (!(proposedEnd.isBefore(s) || proposedStart.isAfter(e) || proposedEnd.equals(s) || proposedStart.equals(e))) { out.add("Error: Staff double-booked (overlap)"); return out; }
        }
        a.dateTime = dt; LogManager.log("reschedule_appt " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Appointment rescheduled " + id);
        return out;
    }

    public static int countForDoctorOn(String doctorId, java.time.LocalDate date) {
        int c = 0; for (Appointment a : DataStore.appointments.values()) if (doctorId.equals(a.staffId) && a.dateTime.toLocalDate().equals(date) && !a.isCompleted) c++; return c;
    }

    public static List<String> rescheduleDoctorDay(String doctorId, java.time.LocalDate day, java.time.LocalDate newDate, java.time.LocalTime newStartTime) {
        List<String> out = new ArrayList<>();
        java.util.List<Appointment> list = new java.util.ArrayList<>();
        for (Appointment a : DataStore.appointments.values()) if (doctorId.equals(a.staffId) && a.dateTime.toLocalDate().equals(day) && !a.isCompleted) list.add(a);
        if (list.isEmpty()) { out.add("No appointments to reschedule"); return out; }
        list.sort(java.util.Comparator.comparing(a -> a.dateTime));
        java.time.LocalDate targetDate = (newDate == null ? day : newDate);
        java.time.LocalTime base = (newStartTime == null ? list.get(0).dateTime.toLocalTime() : newStartTime);
        for (int idx = 0; idx < list.size(); idx++) {
            Appointment a = list.get(idx);
            java.time.LocalDateTime proposed = java.time.LocalDateTime.of(targetDate, base);
            boolean conflict = true; int guard = 0;
            while (conflict && guard < 48) {
                conflict = false;
                java.time.LocalDateTime ps = proposed; java.time.LocalDateTime pe = proposed.plusHours(1);
                for (Appointment x : DataStore.appointments.values()) {
                    if (!x.id.equals(a.id) && doctorId.equals(x.staffId) && x.dateTime.toLocalDate().equals(targetDate)) {
                        java.time.LocalDateTime xs = x.dateTime; java.time.LocalDateTime xe = x.dateTime.plusHours(1);
                        if (!(pe.isBefore(xs) || ps.isAfter(xe) || pe.equals(xs) || ps.equals(xe))) { conflict = true; break; }
                    }
                }
                if (conflict) { proposed = proposed.plusHours(1); }
                guard++;
            }
            a.dateTime = proposed;
            a.notes = (a.notes==null?"":"[Rescheduled] ") + "Rescheduled by doctor";
            base = proposed.toLocalTime().plusHours(1);
        }
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Rescheduled " + list.size() + " appointment(s)");
        return out;
    }
}
