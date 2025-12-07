# HPMS Database Migration Guide

## Overview
This guide helps you migrate the HPMS application from in-memory storage (DataStore) to MySQL database persistence.

## Prerequisites
1. ✅ MySQL Server running (XAMPP includes MySQL)
2. ✅ MySQL JDBC Driver in lib/ folder (mysql-connector-j-9.5.0.jar)
3. ✅ Database schema SQL script created
4. ✅ DBConnection.java utility class created

## Step 1: Create the Database

1. Start MySQL server (through XAMPP Control Panel)
2. Open MySQL command line or phpMyAdmin
3. Run the database schema script:

```bash
# From command line:
cd C:\xampp\htdocs\HPMS
mysql -u root -p < database_schema.sql

# Or in phpMyAdmin:
# - Click "Import" tab
# - Choose file: database_schema.sql
# - Click "Go"
```

This will:
- Drop existing hpms_db database (if any)
- Create fresh hpms_db database
- Create all 30+ tables
- Insert default departments

## Step 2: Update VS Code Settings

The .vscode/settings.json has been created with the JDBC driver reference.

## Step 3: Service Migration Status

The following services need database implementation:

### Critical Services (Highest Priority)
- [ ] AuthService - User authentication and registration
- [ ] PatientService - Patient CRUD operations
- [ ] StaffService - Staff management
- [ ] AppointmentService - Appointment scheduling
- [ ] BillingService - Billing and payments
- [ ] RoomService - Room management

### Secondary Services
- [ ] PrescriptionService
- [ ] MedicineService
- [ ] LabService
- [ ] PatientStatusService
- [ ] DoctorScheduleService
- [ ] DischargeService
- [ ] CommunicationService
- [ ] InventoryService
- [ ] ReportService

## Step 4: Migration Approach

### Option A: Gradual Migration (Recommended)
1. Keep DataStore for backward compatibility
2. Add database methods alongside existing methods
3. Test each service individually
4. Switch to database once tested
5. Remove DataStore dependencies last

### Option B: Complete Rewrite (Faster but Riskier)
1. Rewrite all services to use database
2. Remove DataStore dependencies
3. Test entire application
4. May break existing functionality temporarily

## Step 5: Code Changes Required

Each service file needs:

1. **Import SQL classes**
```java
import java.sql.*;
import hpms.util.DBConnection;
```

2. **Replace DataStore.map.get() with SQL SELECT**
```java
// OLD:
Patient p = DataStore.patients.get(id);

// NEW:
Patient p = null;
try (Connection conn = DBConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients WHERE id = ?")) {
    stmt.setString(1, id);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
        p = mapResultSetToPatient(rs);
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

3. **Replace DataStore.map.put() with SQL INSERT/UPDATE**
```java
// OLD:
DataStore.patients.put(patient.id, patient);

// NEW:
try (Connection conn = DBConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(
         "INSERT INTO patients (id, name, age, gender, contact, address, created_at) " +
         "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
    stmt.setString(1, patient.id);
    stmt.setString(2, patient.name);
    stmt.setInt(3, patient.age);
    stmt.setString(4, patient.gender.name());
    stmt.setString(5, patient.contact);
    stmt.setString(6, patient.address);
    stmt.setTimestamp(7, Timestamp.valueOf(patient.createdAt));
    stmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();
}
```

4. **Replace DataStore.map.values() with SQL SELECT ALL**
```java
// OLD:
Collection<Patient> patients = DataStore.patients.values();

// NEW:
List<Patient> patients = new ArrayList<>();
try (Connection conn = DBConnection.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {
    while (rs.next()) {
        patients.add(mapResultSetToPatient(rs));
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

## Step 6: Testing Strategy

1. **Run database script** - Verify tables created
2. **Test DBConnection** - Verify connection works
3. **Seed admin user** - Test AuthService.seedAdmin()
4. **Test each service** - Use existing test files
5. **Run Verifier.java** - Full integration test
6. **Test UI** - Launch MainGUI and test workflows

## Step 7: Backup Strategy

Since you're moving from BackupUtil (JSON) to database:
- Database has built-in persistence
- Use MySQL backup tools: mysqldump
- Consider keeping BackupUtil for export/import features

## Common Issues

### Issue 1: "java.sql classes not accessible"
**Solution:** Ensure mysql-connector-j-9.5.0.jar is in lib/ and referenced in .vscode/settings.json

### Issue 2: "Connection refused"
**Solution:** Start MySQL server in XAMPP Control Panel

### Issue 3: "Access denied for user 'root'@'localhost'"
**Solution:** Update DBConnection.java with correct username/password

### Issue 4: "Unknown database 'hpms_db'"
**Solution:** Run the database_schema.sql script first

## Next Steps

1. **Review this guide** - Understand the migration process
2. **Run database script** - Create the database structure
3. **Choose migration approach** - Gradual vs Complete
4. **Start with AuthService** - Most critical service
5. **Test thoroughly** - Each service before moving to next
6. **Update UI code** - May need changes for database errors

## Timeline Estimate

- Database setup: 15 minutes
- Per service migration: 1-2 hours
- Total for all services: 20-30 hours
- Testing and bug fixes: 5-10 hours
- **Total project time: 25-40 hours**

## Getting Help

If you need help:
1. Check MySQL error logs: C:\xampp\mysql\data\*.err
2. Check Java console output for SQL errors
3. Test queries directly in phpMyAdmin
4. Review this guide for common solutions

---

**Created:** December 7, 2025
**Status:** Schema and infrastructure ready, services need migration
