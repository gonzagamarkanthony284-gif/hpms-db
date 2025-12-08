package hpms.test;

import hpms.service.PatientService;
import hpms.model.Patient;
import hpms.util.DataStore;
import java.util.List;

public class PatientEditTypeTest {
    public static void main(String[] args) throws Exception {
        List<String> out = PatientService.add("EditTypeTest", "35", "1988-12-05", "Female", "777-7777", "Addr",
                "OUTPATIENT");
        System.out.println(out);
        String id = out.get(0).split(" ")[2];
        // edit to set registration type
        List<String> res = PatientService.editExtended(id, "EditTypeTest", "35", "1988-12-05", "Female", "777-7777",
                "Addr", "OUTPATIENT",
                "Emergency Patient", "12:30", "Ambulance", "120/80", "80", "98", "Chest pain",
                "None", "None", "No past history", "Never", "Never", "Unemployed",
                "None", "", "", "", "");
        System.out.println(res);
        Patient p = DataStore.patients.get(id);
        System.out.println("Registration type: " + p.registrationType);
        if (!"Emergency Patient".equals(p.registrationType)) {
            System.err.println("Edit failed");
            System.exit(2);
        }
        System.out.println("OK: registration type updated");
    }
}
