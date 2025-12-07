package hpms.test;

import hpms.service.StaffService;
import hpms.util.DataStore;

public class StaffServiceTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting StaffService quick tests...");

        // Ensure clean slate
        DataStore.staff.clear();

        // 1) Add valid staff
        java.util.List<String> out = StaffService.add("Test Doctor","DOCTOR","Cardiology","Cardiology","+1234567890","doc1@example.com","LIC-100","MD","Notes");
        System.out.println(out);
        if (out.isEmpty() || !out.get(0).startsWith("Staff added")) { System.err.println("Failed to add staff"); System.exit(2); }

        // 2) Duplicate by name+role+dept should be rejected
        java.util.List<String> out2 = StaffService.add("Test Doctor","DOCTOR","Cardiology","Cardiology","+1234567899","doc2@example.com","LIC-101","MD","Notes");
        System.out.println(out2);
        if (out2.isEmpty() || !out2.get(0).toLowerCase().contains("already exists")) { System.err.println("Duplicate name+role+dept not rejected"); System.exit(3); }

        // 3) Duplicate email is rejected
        java.util.List<String> out3 = StaffService.add("New Doctor","DOCTOR","Cardiology","Cardiology","+1234567800","doc1@example.com","LIC-102","MD","Notes");
        System.out.println(out3);
        if (out3.isEmpty() || !out3.get(0).toLowerCase().contains("same email")) { System.err.println("Duplicate email not rejected"); System.exit(4); }

        // 4) Invalid email rejected
        java.util.List<String> out4 = StaffService.add("Bad Email","NURSE","Nursing","Spec","+1234567801","not-an-email","LIC-103","RN","Notes");
        System.out.println(out4);
        if (out4.isEmpty() || !out4.get(0).toLowerCase().contains("invalid email")) { System.err.println("Invalid email not detected"); System.exit(5); }

        // 5) Invalid phone rejected
        java.util.List<String> out5 = StaffService.add("Bad Phone","CASHIER","Billing","Spec","abcd-***","phone@example.com","LIC-104","Cert","Notes");
        System.out.println(out5);
        if (out5.isEmpty() || !out5.get(0).toLowerCase().contains("invalid phone")) { System.err.println("Invalid phone not detected"); System.exit(6); }

        System.out.println("StaffService quick tests OK");
    }
}
