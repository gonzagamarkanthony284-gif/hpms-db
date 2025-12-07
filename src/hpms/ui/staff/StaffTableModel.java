package hpms.ui.staff;

import hpms.model.Staff;
import hpms.model.StaffRole;
import hpms.util.DataStore;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying staff with role-based filtering
 * Columns: Staff ID, Name, Department, Role, Status, Joined Date, View Profile
 */
public class StaffTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Staff ID", "Name", "Department", "Role", "Status", "Joined Date", "Action"};
    private final List<Staff> staffList = new ArrayList<>();
    private StaffRole filterRole;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public StaffTableModel() {
        this.filterRole = null;
    }

    public void setFilterRole(StaffRole role) {
        this.filterRole = role;
        refreshData();
    }

    public void refreshData() {
        staffList.clear();
        
        for (Staff staff : DataStore.staff.values()) {
            if (filterRole == null || staff.role == filterRole) {
                staffList.add(staff);
            }
        }
        
        // Sort by staff ID for consistency
        staffList.sort((a, b) -> a.id.compareTo(b.id));
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return staffList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row < 0 || row >= staffList.size()) {
            return null;
        }
        
        Staff staff = staffList.get(row);
        
        switch (column) {
            case 0: return staff.id;
            case 1: return staff.name;
            case 2: return staff.department;
            case 3: return staff.role != null ? staff.role.toString() : "N/A";
            case 4: return staff.isAvailable ? "Active" : "Inactive";
            case 5: return staff.createdAt != null ? staff.createdAt.format(DATE_FORMATTER) : "N/A";
            case 6: return "View";
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Get staff at the specified row
     */
    public Staff getStaffAt(int row) {
        if (row < 0 || row >= staffList.size()) {
            return null;
        }
        return staffList.get(row);
    }

    /**
     * Update a staff member in the model
     */
    public void updateStaff(Staff staff) {
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).id.equals(staff.id)) {
                staffList.set(i, staff);
                fireTableRowsUpdated(i, i);
                return;
            }
        }
    }

    /**
     * Add a new staff member to the model
     */
    public void addStaff(Staff staff) {
        if (filterRole == null || staff.role == filterRole) {
            staffList.add(staff);
            staffList.sort((a, b) -> a.id.compareTo(b.id));
            fireTableDataChanged();
        }
    }

    /**
     * Remove staff member from the model
     */
    public void removeStaff(String staffId) {
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).id.equals(staffId)) {
                staffList.remove(i);
                fireTableRowsDeleted(i, i);
                return;
            }
        }
    }

    /**
     * Get total count of all staff (ignoring filter)
     */
    public int getTotalStaffCount() {
        return DataStore.staff.size();
    }

    /**
     * Get count of filtered staff
     */
    public int getFilteredCount() {
        return staffList.size();
    }
}
