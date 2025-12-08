package hpms.test;

import hpms.service.AppointmentService;
import hpms.util.DataStore;
import hpms.util.IDGenerator;
import hpms.model.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentTest {
    public static void main(String[] args) {
        // create staff and patient
        String pid = IDGenerator.nextId("P");
        Patient p = new Patient(pid, "Test Patient", 30, "1995-05-15", Gender.Male, "123", "Addr", LocalDateTime.now());
        DataStore.patients.put(pid, p);
        String sid = IDGenerator.nextId("S");
        Staff s = new Staff(sid, "Dr Who", StaffRole.DOCTOR, "ER", LocalDateTime.now());
        DataStore.staff.put(sid, s);

        String date = LocalDate.now().plusDays(1).toString();
        String time = LocalTime.of(9, 0).toString();
        List<String> out1 = AppointmentService.schedule(pid, sid, date, time, "ER");
        System.out.println(out1);
        // try overlapping slot (9:30) should be rejected for 1-hour overlap
        List<String> out2 = AppointmentService.schedule(pid, sid, date, LocalTime.of(9, 30).toString(), "ER");
        System.out.println(out2);
        // try a free slot (10:00) should be accepted
        List<String> out3 = AppointmentService.schedule(pid, sid, date, LocalTime.of(10, 0).toString(), "ER");
        System.out.println(out3);
    }
}
