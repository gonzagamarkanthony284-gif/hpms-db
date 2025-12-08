package hpms.test;

import hpms.service.PatientService;
import hpms.model.Patient;
import hpms.util.DataStore;
import java.util.List;
import java.util.ArrayList;

public class PatientAttachmentsTest {
    public static void main(String[] args) throws Exception {
        List<String> out = PatientService.add("AttachTest", "30", "1990-06-10", "Male", "666-6666", "Addr",
                "INPATIENT");
        System.out.println(out);
        String id = out.get(0).split(" ")[2];
        List<String> attaches = new ArrayList<>();
        attaches.add("C:/tmp/report1.pdf");
        attaches.add("C:/tmp/xray1.jpg");
        // call with simple, explicit nulls for string fields and pass attachments list
        List<String> res = PatientService.addClinicalInfo(id, null, null, null, "note", "tester",
                null, null, null, null, null, null, null, null, null, null, null, null, attaches);
        System.out.println(res);
        Patient p = DataStore.patients.get(id);
        System.out.println("Attachments: " + p.attachmentPaths);
        if (p.attachmentPaths == null || p.attachmentPaths.size() < 2) {
            System.err.println("Attachment persistence failed");
            System.exit(2);
        }
        System.out.println("OK: attachments persisted");
    }
}
