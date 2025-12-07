package hpms.model;

import java.io.Serializable;

public class Service implements Serializable {
    private static final long serialVersionUID = 1L;

    public String serviceId;
    public String serviceName;
    public String description;
    public String status; // ACTIVE, INACTIVE
    public int availableBeds;
    public int totalBeds;
    public String department;
    public long createdAt;

    public Service() {
        this.serviceId = "";
        this.serviceName = "";
        this.description = "";
        this.status = "ACTIVE";
        this.availableBeds = 0;
        this.totalBeds = 0;
        this.department = "";
        this.createdAt = System.currentTimeMillis();
    }

    public Service(String serviceId, String serviceName, String description, 
                   int availableBeds, int totalBeds, String department) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = "ACTIVE";
        this.availableBeds = availableBeds;
        this.totalBeds = totalBeds;
        this.department = department;
        this.createdAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", availableBeds=" + availableBeds +
                ", totalBeds=" + totalBeds +
                ", department='" + department + '\'' +
                '}';
    }
}
