package hpms.service;

import hpms.model.*;
import hpms.util.*;
import java.util.*;

public class MedicineService {
    
    public static List<String> addMedicine(String name, String genericName, double price, String dosageForm, String strength) {
        List<String> out = new ArrayList<>();
        
        if (Validators.empty(name) || Validators.empty(genericName)) {
            out.add("Error: Missing required fields");
            return out;
        }
        
        String id = IDGenerator.nextId("M");
        Medicine med = new Medicine(id, name, genericName, price);
        med.dosageForm = dosageForm;
        med.strength = strength;
        
        DataStore.medicines.put(id, med);
        DataStore.log("add_medicine " + id);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Medicine added " + id);
        return out;
    }
    
    public static List<String> updateStock(String medicineId, int quantity, String action) {
        List<String> out = new ArrayList<>();
        
        Medicine med = DataStore.medicines.get(medicineId);
        if (med == null) {
            out.add("Error: Medicine not found");
            return out;
        }
        
        if ("ADD".equalsIgnoreCase(action)) {
            med.stockQuantity += quantity;
        } else if ("REMOVE".equalsIgnoreCase(action)) {
            if (med.stockQuantity < quantity) {
                out.add("Error: Insufficient stock");
                return out;
            }
            med.stockQuantity -= quantity;
        } else {
            out.add("Error: Invalid action");
            return out;
        }
        
        DataStore.log("update_medicine_stock " + medicineId + " " + action + " " + quantity);
        try { BackupUtil.saveToDefault(); } catch (Exception ex) { }
        out.add("Stock updated. Current: " + med.stockQuantity);
        return out;
    }
    
    public static List<String> getLowStockMedicines() {
        List<String> out = new ArrayList<>();
        
        for (Medicine med : DataStore.medicines.values()) {
            if (med.needsRestocking()) {
                out.add(med.id + ": " + med.name + " (Stock: " + med.stockQuantity + ", Min: " + med.minimumStockLevel + ")");
            }
        }
        
        if (out.isEmpty()) out.add("All medicines are adequately stocked");
        return out;
    }
    
    public static List<String> searchMedicine(String searchTerm) {
        List<String> out = new ArrayList<>();
        
        for (Medicine med : DataStore.medicines.values()) {
            if (med.name.toLowerCase().contains(searchTerm.toLowerCase()) ||
                med.genericName.toLowerCase().contains(searchTerm.toLowerCase())) {
                out.add(med.id + ": " + med.name + " (" + med.genericName + ") - " + med.strength + " - Stock: " + med.stockQuantity);
            }
        }
        
        if (out.isEmpty()) out.add("No medicines found");
        return out;
    }
}
