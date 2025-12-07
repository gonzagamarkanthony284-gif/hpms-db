package hpms.model;

public class LabTestType {
    public final String id;
    public String name;              // e.g., "Complete Blood Count"
    public String code;              // e.g., "CBC"
    public String description;
    public double cost;
    public String category;          // Hematology, Biochemistry, etc.
    public String normalRange;       // Reference range for normal results
    public String unit;              // mg/dL, cells/mcL, etc.
    public int turnaroundTimeDays;   // How long for results
    public String sampleType;        // Blood, Urine, etc.
    public boolean isActive = true;
    
    public LabTestType(String id, String name, String code, double cost) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cost = cost;
        this.turnaroundTimeDays = 1;
        this.sampleType = "Blood";
    }
}
