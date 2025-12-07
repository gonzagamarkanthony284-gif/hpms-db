package hpms.model;

public class Medicine {
    public final String id;
    public String name;
    public String genericName;
    public String manufacturer;
    public double price;
    public int stockQuantity;
    public int minimumStockLevel;
    public String dosageForm;  // Tablet, Capsule, Syrup, Injection, etc.
    public String strength;     // e.g., "500mg"
    public String expireDate;   // YYYY-MM-DD
    public String category;     // Antibiotic, Painkiller, etc.
    public String description;
    
    public Medicine(String id, String name, String genericName, double price) {
        this.id = id;
        this.name = name;
        this.genericName = genericName;
        this.price = price;
        this.stockQuantity = 0;
        this.minimumStockLevel = 10;
    }
    
    public boolean isInStock() {
        return stockQuantity > 0;
    }
    
    public boolean needsRestocking() {
        return stockQuantity <= minimumStockLevel;
    }
}
