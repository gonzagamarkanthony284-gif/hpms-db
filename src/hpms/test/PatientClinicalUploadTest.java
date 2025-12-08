package hpms.test;

import hpms.service.PatientService;
import hpms.util.DataStore;
import hpms.model.Patient;
import java.util.List;

public class PatientClinicalUploadTest {
    public static void main(String[] args) throws Exception {
        // create a patient
        List<String> out = PatientService.add("ClinicalTest", "40", "1985-03-20", "Male", "555-0000", "Test Address",
                "INPATIENT");
        System.out.println(out);
        String created = out.get(0);
        String id = created.split(" ")[2];
        // add clinical info and uploads
        List<String> res = PatientService.addClinicalInfo(id, "170", "70", "120/80", "Progress ok", "tester",
                "C:\\tmp\\xray.jpg", "Uploaded", "Broken Bone; Location:Right Arm; Severity:Moderate",
                "C:\\tmp\\stool.pdf", "Uploaded", "Parasites: None; Bacteria: Abnormal (E. coli)",
                "C:\\tmp\\urine.pdf", "Reviewed", "Cloudy; WBC High; UTI suspected",
                "C:\\tmp\\blood.pdf", "Critical", "Elevated WBC; CRP high; Infection suspected", null);
        System.out.println(res);
        Patient p = DataStore.patients.get(id);
        System.out.println("X-ray: " + p.xrayFilePath + " -> " + p.xraySummary + " (" + p.xrayStatus + ")");
        System.out.println("Stool: " + p.stoolFilePath + " -> " + p.stoolSummary + " (" + p.stoolStatus + ")");
        System.out.println("Urine: " + p.urineFilePath + " -> " + p.urineSummary + " (" + p.urineStatus + ")");
        System.out.println("Blood: " + p.bloodFilePath + " -> " + p.bloodSummary + " (" + p.bloodStatus + ")");
    }
}
