package hpms.service;

import hpms.model.Service;
import hpms.util.IDGenerator;
import hpms.util.DataStore;

import java.util.*;

public class ServiceService {
    private static Map<String, Service> services = DataStore.services;

    static {
        initializeDefaultServices();
    }

    public static void initializeDefaultServices() {
        if (!services.isEmpty()) return;

        String[][] defaultServices = {
                {"Cardiology", "Cardiology Department - Heart and Cardiovascular Services", "10", "15"},
                {"Neurology", "Neurology Department - Brain and Nervous System Disorders", "8", "12"},
                {"Orthopedics", "Orthopedics Department - Bone and Joint Surgery", "12", "20"},
                {"Pediatrics", "Pediatrics Department - Child Healthcare Services", "15", "25"},
                {"Oncology", "Oncology Department - Cancer Treatment and Therapy", "6", "10"},
                {"ER", "Emergency Room - 24/7 Emergency Medical Services", "20", "30"}
        };

        for (String[] serviceData : defaultServices) {
            String serviceId = IDGenerator.nextId("SVC");
            Service service = new Service(
                    serviceId,
                    serviceData[0],
                    serviceData[1],
                    Integer.parseInt(serviceData[2]),
                    Integer.parseInt(serviceData[3]),
                    serviceData[0]
            );
            services.put(serviceId, service);
        }
    }

    public static Service addService(String serviceName, String description, 
                                      int availableBeds, int totalBeds, String department) {
        String serviceId = IDGenerator.nextId("SVC");
        Service service = new Service(serviceId, serviceName, description, 
                                       availableBeds, totalBeds, department);
        services.put(serviceId, service);
        return service;
    }

    public static Service getService(String serviceId) {
        return services.get(serviceId);
    }

    public static List<Service> getAllServices() {
        return new ArrayList<>(services.values());
    }

    public static Map<String, Service> getServicesMap() {
        return services;
    }

    public static boolean updateService(String serviceId, String serviceName, String description,
                                         int availableBeds, int totalBeds, String status) {
        Service service = services.get(serviceId);
        if (service == null) return false;

        service.serviceName = serviceName;
        service.description = description;
        service.availableBeds = availableBeds;
        service.totalBeds = totalBeds;
        service.status = status;

        return true;
    }

    public static boolean deleteService(String serviceId) {
        return services.remove(serviceId) != null;
    }

    public static List<Service> getActiveServices() {
        List<Service> activeServices = new ArrayList<>();
        for (Service service : services.values()) {
            if ("ACTIVE".equals(service.status)) {
                activeServices.add(service);
            }
        }
        return activeServices;
    }

    public static int getTotalAvailableBeds() {
        int total = 0;
        for (Service service : services.values()) {
            total += service.availableBeds;
        }
        return total;
    }

    public static Service getServiceByName(String serviceName) {
        for (Service service : services.values()) {
            if (service.serviceName.equalsIgnoreCase(serviceName)) {
                return service;
            }
        }
        return null;
    }

    public static List<Service> searchServices(String query) {
        List<Service> results = new ArrayList<>();
        String queryLower = query.toLowerCase();
        for (Service service : services.values()) {
            if (service.serviceName.toLowerCase().contains(queryLower) ||
                    service.description.toLowerCase().contains(queryLower) ||
                    service.department.toLowerCase().contains(queryLower)) {
                results.add(service);
            }
        }
        return results;
    }

    public static void updateAvailableBeds(String serviceId, int newCount) {
        Service service = services.get(serviceId);
        if (service != null) {
            service.availableBeds = newCount;
        }
    }

    public static void setServices(Map<String, Service> newServices) {
        services.clear();
        services.putAll(newServices);
    }
}
