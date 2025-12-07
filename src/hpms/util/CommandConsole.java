package hpms.util;

import hpms.auth.AuthService;
import hpms.service.*;
import hpms.model.*;

import javax.swing.*;
import java.util.*;

public class CommandConsole {
    public static List<String> execute(String input) {
        String[] t = input.trim().split("\\s+"); if (t.length==0) return Collections.singletonList("Error: Missing command");
        String cmd = t[0].toLowerCase(java.util.Locale.ROOT);
        if (cmd.equals("login")) { if (t.length<3) return Collections.singletonList("Error: Missing parameters"); return AuthService.login(t[1], t[2]); }
        if (cmd.equals("logout")) { return AuthService.logout(); }
        if (cmd.equals("register_user")) { if (t.length<4) return Collections.singletonList("Error: Missing parameters"); return AuthService.register(t[1], t[2], t[3]); }
        if (cmd.equals("add_patient")) { if (t.length<6) return Collections.singletonList("Error: Missing parameters"); return PatientService.add(t[1], t[2], t[3], t[4], joinRest(t,5)); }
        if (cmd.equals("list_patients")) { List<String> out=new ArrayList<>(); for (Patient p: DataStore.patients.values()) out.add(p.id+" "+p.name); if (out.isEmpty()) out.add("No patients"); return out; }
        if (cmd.equals("add_staff")) { if (t.length<4) return Collections.singletonList("Error: Missing parameters"); return StaffService.add(t[1], t[2], joinRest(t,3)); }
        if (cmd.equals("list_staff")) { List<String> out=new ArrayList<>(); for (Staff s: DataStore.staff.values()) out.add(s.id+" "+s.name+" "+s.role+" "+s.department); if (out.isEmpty()) out.add("No staff"); return out; }
        if (cmd.equals("schedule_appt")) { if (t.length<6) return Collections.singletonList("Error: Missing parameters"); return AppointmentService.schedule(t[1], t[2], t[3], t[4], joinRest(t,5)); }
        if (cmd.equals("list_appts")) { List<String> out=new ArrayList<>(); for (Appointment a: DataStore.appointments.values()) out.add(a.id+" "+a.patientId+" "+a.staffId+" "+a.dateTime); if (out.isEmpty()) out.add("No appointments"); return out; }
        if (cmd.equals("create_bill")) { if (t.length<3) return Collections.singletonList("Error: Missing parameters"); return BillingService.create(t[1], t[2]); }
        if (cmd.equals("pay_bill")) { if (t.length<3) return Collections.singletonList("Error: Missing parameters"); return BillingService.pay(t[1], t[2]); }
        if (cmd.equals("assign_room")) { if (t.length<3) return Collections.singletonList("Error: Missing parameters"); return RoomService.assign(t[1], t[2]); }
        if (cmd.equals("list_rooms")) { List<String> out=new ArrayList<>(); for (Room r: DataStore.rooms.values()) out.add(r.id+" "+r.status+(r.occupantPatientId==null?"":" "+r.occupantPatientId)); return out; }
        if (cmd.equals("backup")) { String json = BackupUtil.toJson(); return Collections.singletonList(json); }
        if (cmd.equals("restore")) { JTextArea area=new JTextArea(10,40); int r=JOptionPane.showConfirmDialog(null,new JScrollPane(area),"Paste backup JSON",JOptionPane.OK_CANCEL_OPTION); if (r==JOptionPane.OK_OPTION) { BackupUtil.fromJson(area.getText()); return Collections.singletonList("Restore complete"); } return Collections.singletonList("Canceled"); }
        if (cmd.equals("help")) { return Arrays.asList("login <user> <pass>", "logout", "register_user <user> <pass> <role>", "add_patient <name> <age> <gender> <contact> <address>", "list_patients", "add_staff <name> <role> <department>", "list_staff", "schedule_appt <pid> <sid> <date> <time> <dept>", "list_appts", "create_bill <pid> <amount>", "pay_bill <billId> <method>", "assign_room <roomId> <pid>", "list_rooms", "backup", "restore", "help"); }
        return Collections.singletonList("Error: Unknown command");
    }
    static String joinRest(String[] arr, int start) { StringBuilder sb=new StringBuilder(); for (int i=start;i<arr.length;i++){ if (sb.length()>0) sb.append(' '); sb.append(arr[i]); } return sb.toString(); }
}

