package hpms.service;

import hpms.model.*;
import hpms.util.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LabService {
    public static List<String> request(String patientId, String doctorId, String testName) {
        List<String> out = new ArrayList<>();
        if (Validators.empty(patientId) || Validators.empty(doctorId) || Validators.empty(testName)) { out.add("Error: Missing parameters"); return out; }
        if (!DataStore.patients.containsKey(patientId)) { out.add("Error: Patient not found"); return out; }
        if (!DataStore.staff.containsKey(doctorId)) { out.add("Error: Doctor not found"); return out; }
        String id = "L" + DataStore.lCounter.incrementAndGet();
        LabTestRequest req = new LabTestRequest(id, patientId, doctorId, testName.trim(), LocalDateTime.now());
        DataStore.labTests.put(id, req); LogManager.log("lab_request " + id);
        out.add("Lab test requested " + id);
        return out;
    }
    public static List<String> enterResult(String requestId, String resultText) {
        List<String> out = new ArrayList<>(); LabTestRequest req = DataStore.labTests.get(requestId);
        if (req == null) { out.add("Error: Request not found"); return out; }
        String id = "LR" + DataStore.lCounter.incrementAndGet();
        LabResult res = new LabResult(id, requestId, resultText==null?"":resultText.trim(), LocalDateTime.now());
        DataStore.labResults.put(id, res); LogManager.log("lab_result " + id);
        out.add("Lab result entered " + id);
        return out;
    }
    public static List<String> listRequests() { List<String> out = new ArrayList<>(); for (LabTestRequest r : DataStore.labTests.values()) out.add(r.id + " " + r.patientId + " " + r.doctorId + " " + r.testName + " " + r.requestedAt); if (out.isEmpty()) out.add("No lab requests"); return out; }
    public static List<String> listResults() { List<String> out = new ArrayList<>(); for (LabResult r : DataStore.labResults.values()) out.add(r.id + " from " + r.testRequestId + ": " + r.resultText + " (" + r.enteredAt + ")"); if (out.isEmpty()) out.add("No lab results"); return out; }
}

