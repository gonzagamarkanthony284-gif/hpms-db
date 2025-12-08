package hpms.ui.components;

import hpms.util.DataStore;
import hpms.model.Staff;
import hpms.model.StaffRole;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Doctor Filter Panel Component
 * Provides filtering capabilities for doctor listings by department and
 * specialization.
 * Features:
 * - Department filter dropdown
 * - Specialization filter dropdown
 * - Reset button to clear all filters
 * - Callback support for filter changes
 */
public class DoctorFilterPanel extends JPanel {
    private JComboBox<String> departmentCombo;
    private JComboBox<String> specialtyCombo;
    private JButton resetButton;
    private FilterChangeListener filterListener;

    /**
     * Interface for listening to filter changes
     */
    public interface FilterChangeListener {
        void onFilterChanged(String department, String specialty);
    }

    /**
     * Constructor
     */
    public DoctorFilterPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Filter Doctors"));
        setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Department label and combo
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        departmentCombo = new JComboBox<>();
        departmentCombo.addItem("-- All Departments --");
        departmentCombo.addActionListener(e -> fireFilterChanged());
        add(departmentCombo, gbc);

        // Specialization label and combo
        gbc.gridx = 2;
        add(new JLabel("Specialization:"), gbc);

        gbc.gridx = 3;
        specialtyCombo = new JComboBox<>();
        specialtyCombo.addItem("-- All Specializations --");
        specialtyCombo.addActionListener(e -> fireFilterChanged());
        add(specialtyCombo, gbc);

        // Reset button
        gbc.gridx = 4;
        resetButton = new JButton("Reset Filters");
        resetButton.addActionListener(e -> resetFilters());
        add(resetButton, gbc);

        // Populate filters from DataStore
        populateFilters();
    }

    /**
     * Populate filter dropdowns from current DataStore data
     */
    private void populateFilters() {
        // Clear existing items except first placeholder
        while (departmentCombo.getItemCount() > 1) {
            departmentCombo.removeItemAt(1);
        }
        while (specialtyCombo.getItemCount() > 1) {
            specialtyCombo.removeItemAt(1);
        }

        // Collect unique departments and specialties from doctors
        Set<String> departments = new TreeSet<>();
        Set<String> specialties = new TreeSet<>();

        for (Staff staff : DataStore.staff.values()) {
            if (staff.role == StaffRole.DOCTOR) {
                if (staff.department != null && !staff.department.isEmpty()) {
                    departments.add(staff.department);
                }
                if (staff.specialty != null && !staff.specialty.isEmpty()) {
                    specialties.add(staff.specialty);
                }
            }
        }

        // Add departments to combo
        for (String dept : departments) {
            departmentCombo.addItem(dept);
        }

        // Add specialties to combo
        for (String specialty : specialties) {
            specialtyCombo.addItem(specialty);
        }
    }

    /**
     * Reset all filters to default state
     */
    private void resetFilters() {
        departmentCombo.setSelectedIndex(0);
        specialtyCombo.setSelectedIndex(0);
        fireFilterChanged();
    }

    /**
     * Get selected department filter
     * 
     * @return Selected department or null for "all"
     */
    public String getSelectedDepartment() {
        int index = departmentCombo.getSelectedIndex();
        if (index <= 0)
            return null;
        return (String) departmentCombo.getSelectedItem();
    }

    /**
     * Get selected specialization filter
     * 
     * @return Selected specialization or null for "all"
     */
    public String getSelectedSpecialty() {
        int index = specialtyCombo.getSelectedIndex();
        if (index <= 0)
            return null;
        return (String) specialtyCombo.getSelectedItem();
    }

    /**
     * Set filter change listener
     * 
     * @param listener Callback for filter changes
     */
    public void setFilterChangeListener(FilterChangeListener listener) {
        this.filterListener = listener;
    }

    /**
     * Notify listener of filter changes
     */
    private void fireFilterChanged() {
        if (filterListener != null) {
            filterListener.onFilterChanged(getSelectedDepartment(), getSelectedSpecialty());
        }
    }

    /**
     * Refresh filter options from updated DataStore
     */
    public void refreshFilters() {
        populateFilters();
    }
}
