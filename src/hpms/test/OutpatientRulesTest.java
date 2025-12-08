package hpms.test;

import hpms.model.*;
import hpms.service.*;
import hpms.util.DataStore;
import java.time.LocalDateTime;
import java.util.List;

public class OutpatientRulesTest {
    public static void main(String[] args) {
        System.out.println("Testing Outpatient Rules...");

        // Clear data for clean test
        DataStore.patients.clear();
        DataStore.rooms.clear();
        DataStore.patientStatus.clear();
        DataStore.statusHistory.clear();

        // Add a test patient (default OUTPATIENT)
        List<String> addResult = PatientService.add("Test Patient", "30", "1995-05-15", "Male", "1234567890",
                "Test Address", "OUTPATIENT");
        if (addResult.get(0).startsWith("Patient created")) {
            String patientId = addResult.get(0).split(" ")[2];
            System.out.println("Created patient: " + patientId);

            // Add a room
            Room room = new Room("R5001", RoomStatus.VACANT, null);
            DataStore.rooms.put("R5001", room);

            // Test 1: Try to assign room to outpatient - should fail
            List<String> assignResult = RoomService.assign("R5001", patientId);
            if (assignResult.get(0).equals("Error: Outpatients cannot be assigned to rooms")) {
                System.out.println("✓ Test 1 PASSED: Room assignment blocked for outpatient");
            } else {
                System.out.println("✗ Test 1 FAILED: " + assignResult.get(0));
            }

            // Test 2: Change status to INPATIENT and assign room - should succeed
            PatientStatusService.setStatus(patientId, "INPATIENT", "S2001", "Test change");
            assignResult = RoomService.assign("R5001", patientId);
            if (assignResult.get(0).equals("Room assigned")) {
                System.out.println("✓ Test 2 PASSED: Room assignment allowed for inpatient");
            } else {
                System.out.println("✗ Test 2 FAILED: " + assignResult.get(0));
            }

            // Test 3: Try to discharge outpatient on same day without prior inpatient
            // status - should fail
            // First, change back to outpatient
            PatientStatusService.setStatus(patientId, "OUTPATIENT", "S2001", "Test change back");

            // Add a doctor
            Staff doctor = new Staff("S2001", "Dr. Test", StaffRole.DOCTOR, "Cardiology", LocalDateTime.now());
            DataStore.staff.put("S2001", doctor);

            List<String> dischargeResult = DischargeService.discharge(patientId, "R5001", "S2001", "Test Diagnosis",
                    "Test Summary");
            if (dischargeResult.get(0).equals(
                    "Error: Outpatients cannot be discharged on the same day unless status was changed to Inpatient")) {
                System.out.println("✓ Test 3 PASSED: Discharge blocked for outpatient on same day");
            } else {
                System.out.println("✗ Test 3 FAILED: " + dischargeResult.get(0));
            }

            // Test 4: Change to inpatient earlier today and try discharge - should succeed
            PatientStatusService.setStatus(patientId, "INPATIENT", "S2001", "Test inpatient");
            dischargeResult = DischargeService.discharge(patientId, "R5001", "S2001", "Test Diagnosis", "Test Summary");
            if (dischargeResult.get(0).startsWith("Patient discharged successfully")) {
                System.out
                        .println("✓ Test 4 PASSED: Discharge allowed for outpatient with prior inpatient status today");
            } else {
                System.out.println("✗ Test 4 FAILED: " + dischargeResult.get(0));
            }

        } else {
            System.out.println("Failed to create test patient: " + addResult.get(0));
        }

        System.out.println("Testing completed.");
    }
}
