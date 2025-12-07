package hpms.ui.panels;

import hpms.auth.AuthService;
import hpms.model.*;
import hpms.service.*;
import hpms.util.*;
import hpms.ui.components.SectionHeader;
import hpms.ui.components.Theme;

import javax.swing.*;
import hpms.ui.staff.StaffRegistrationForm;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private DefaultTableModel doctorModel, nurseModel, cashierModel;
    private JTable doctorTable, nurseTable, cashierTable;
    private JLabel statsLabel;

    public StaffPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(SectionHeader.info("Staff Management", "Manage staff members organized by role"), BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Create tabbed pane for each role
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(Theme.BG);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Doctor Tab
        tabbedPane.addTab("Doctors", createRolePanel("DOCTOR"));

        // Nurse Tab
        tabbedPane.addTab("Nurses", createRolePanel("NURSE"));

        // Cashier Tab
        tabbedPane.addTab("Cashiers", createRolePanel("CASHIER"));

        // Admin tab removed — admin management lives in Administration section

        add(tabbedPane, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        refresh();

        // Ensure panel refreshes when it becomes visible again
        this.addHierarchyListener(evt -> {
            if ((evt.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (this.isShowing()) SwingUtilities.invokeLater(this::refresh);
            }
        });
    }

    private JPanel createRolePanel(String role) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);

        // Create table model for this role
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Staff ID", "Name", "Department", "Details", "Status", "Joined Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // Store model reference
        if ("DOCTOR".equals(role)) {
            doctorModel = model;
        } else if ("NURSE".equals(role)) {
            nurseModel = model;
        } else if ("CASHIER".equals(role)) {
            cashierModel = model;
        }

        // Create table
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);

        // Store table reference
        if ("DOCTOR".equals(role)) {
            doctorTable = table;
        } else if ("NURSE".equals(role)) {
            nurseTable = table;
        } else if ("CASHIER".equals(role)) {
            cashierTable = table;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));

        statsLabel = new JLabel("Total Staff: 0 | Doctors: 0 | Nurses: 0 | Cashiers: 0");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statsLabel.setForeground(Theme.PRIMARY);

        panel.add(statsLabel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(8, 12, 12, 12));

        JButton addBtn = new JButton("Add Staff");
        styleButton(addBtn, new Color(0, 110, 102));

        JButton editBtn = new JButton("Edit");
        styleButton(editBtn, new Color(41, 128, 185));

        JButton deleteBtn = new JButton("Delete");
        styleButton(deleteBtn, new Color(192, 57, 43));

        JButton regUserBtn = new JButton("Register User");
        styleButton(regUserBtn, new Color(155, 89, 182));

        JButton viewBtn = new JButton("View Details");
        styleButton(viewBtn, new Color(127, 140, 141));

        addBtn.addActionListener(e -> addStaffDialog());
        editBtn.addActionListener(e -> editStaff());
        deleteBtn.addActionListener(e -> deleteStaff());
        regUserBtn.addActionListener(e -> registerUserDialog());
        viewBtn.addActionListener(e -> viewStaffDetails());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        
        if (AuthService.current != null && AuthService.current.role == UserRole.ADMIN) {
            panel.add(regUserBtn);
        }
        
        panel.add(viewBtn);

        return panel;
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addStaffDialog() {
        StaffRegistrationForm form = new StaffRegistrationForm();
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { refresh(); }
            @Override public void windowClosing(java.awt.event.WindowEvent e) { refresh(); }
        });
        form.setVisible(true);
    }

    private JTable getSelectedRoleTable() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex == 0) return doctorTable;
        if (selectedIndex == 1) return nurseTable;
        if (selectedIndex == 2) return cashierTable;
        return null;
    }

    private void editStaff() {
        JTable table = getSelectedRoleTable();
        if (table == null) {
            JOptionPane.showMessageDialog(this, "Please select a staff member", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a staff member to edit", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String staffId = model.getValueAt(row, 0).toString();
        Staff staff = DataStore.staff.get(staffId);

        if (staff == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Staff", true);
        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0.3;
        panel.add(new JLabel("Name"), c);
        c.gridx = 1; c.weightx = 0.7;
        JTextField nameField = new JTextField(staff.name);
        panel.add(nameField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0.3;
        panel.add(new JLabel("Department"), c);
        c.gridx = 1; c.weightx = 0.7;
        JComboBox<String> deptCombo = new JComboBox<>(DataStore.departments.toArray(new String[0]));
        deptCombo.setSelectedItem(staff.department);
        panel.add(deptCombo, c);

        c.gridx = 0; c.gridy = 2; c.weightx = 0.3;
        panel.add(new JLabel("Specialization"), c);
        c.gridx = 1; c.weightx = 0.7;
        JTextField specField = new JTextField(staff.specialty==null?"":staff.specialty);
        panel.add(specField, c);

        c.gridx = 0; c.gridy = 3; c.weightx = 0.3;
        panel.add(new JLabel("Phone"), c);
        c.gridx = 1; c.weightx = 0.7;
        JTextField phoneField = new JTextField(staff.phone==null?"":staff.phone);
        panel.add(phoneField, c);

        c.gridx = 0; c.gridy = 4; c.weightx = 0.3;
        panel.add(new JLabel("Email"), c);
        c.gridx = 1; c.weightx = 0.7;
        JTextField emailField = new JTextField(staff.email==null?"":staff.email);
        panel.add(emailField, c);

        c.gridx = 0; c.gridy = 5; c.weightx = 0.3;
        panel.add(new JLabel("License #"), c);
        c.gridx = 1; c.weightx = 0.7;
        JTextField licenseField = new JTextField(staff.licenseNumber==null?"":staff.licenseNumber);
        panel.add(licenseField, c);

        c.gridx = 0; c.gridy = 6; c.weightx = 0.3;
        panel.add(new JLabel("Qualifications"), c);
        c.gridx = 1; c.weightx = 0.7; c.weighty = 1.0; c.fill = GridBagConstraints.BOTH;
        JTextArea qualsArea = new JTextArea(3, 30);
        qualsArea.setLineWrap(true);
        qualsArea.setWrapStyleWord(true);
        qualsArea.setText(staff.qualifications==null?"":staff.qualifications);
        panel.add(new JScrollPane(qualsArea), c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton saveBtn = new JButton("Update");
        styleButton(saveBtn, new Color(41, 128, 185));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            String nm = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String lic = licenseField.getText().trim();

            if (nm.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                JOptionPane.showMessageDialog(dialog, "Please provide a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!phone.isEmpty() && !phone.matches("^[0-9+()\\-\\s]{7,25}$")) {
                JOptionPane.showMessageDialog(dialog, "Please provide a valid phone number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Staff other : DataStore.staff.values()) {
                if (other.id.equals(staff.id)) continue;
                if (!email.isEmpty() && email.equalsIgnoreCase(other.email)) { JOptionPane.showMessageDialog(dialog, "Another staff member already uses that email.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
                if (!lic.isEmpty() && lic.equalsIgnoreCase(other.licenseNumber)) { JOptionPane.showMessageDialog(dialog, "Another staff member already uses that license number.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            }

            staff.name = nm;
            staff.department = deptCombo.getSelectedItem().toString();
            staff.specialty = specField.getText().trim();
            staff.phone = phoneField.getText().trim();
            staff.email = emailField.getText().trim();
            staff.licenseNumber = licenseField.getText().trim();
            staff.qualifications = qualsArea.getText().trim();
            JOptionPane.showMessageDialog(dialog, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            refresh();
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteStaff() {
        JTable table = getSelectedRoleTable();
        if (table == null) {
            JOptionPane.showMessageDialog(this, "Please select a staff member", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a staff member to delete", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String staffId = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this staff member?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            java.util.List<String> result = StaffService.delete(staffId);
            if (result.get(0).startsWith("Staff deleted")) {
                JOptionPane.showMessageDialog(this, "Staff member deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, result.get(0), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerUserDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Register User Account", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0.3;
        panel.add(new JLabel("Username *"), c);
        c.gridx = 1; c.weightx = 0.7;
        JTextField usernameField = new JTextField();
        panel.add(usernameField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0.3;
        panel.add(new JLabel("Password *"), c);
        c.gridx = 1; c.weightx = 0.7;
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField, c);

        c.gridx = 0; c.gridy = 2; c.weightx = 0.3;
        panel.add(new JLabel("Role *"), c);
        c.gridx = 1; c.weightx = 0.7;
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"DOCTOR", "NURSE", "CASHIER"});
        panel.add(roleCombo, c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton saveBtn = new JButton("Register");
        styleButton(saveBtn, new Color(155, 89, 182));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            if (usernameField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.List<String> result = AuthService.register(
                usernameField.getText().trim(),
                new String(passwordField.getPassword()),
                roleCombo.getSelectedItem().toString()
            );

            if (result.get(0).startsWith("User registered")) {
                JOptionPane.showMessageDialog(dialog, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refresh();
            } else {
                JOptionPane.showMessageDialog(dialog, result.get(0), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void viewStaffDetails() {
        JTable table = getSelectedRoleTable();
        if (table == null) {
            JOptionPane.showMessageDialog(this, "Please select a staff member", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a staff member", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String staffId = model.getValueAt(row, 0).toString();
        Staff staff = DataStore.staff.get(staffId);

        if (staff == null) return;

        String details = String.format(
            "Staff ID: %s\n\n" +
            "Name: %s\n" +
            "Role: %s\n" +
            "Department: %s\n" +
            "Specialization: %s\n" +
            "Phone: %s\n" +
            "Email: %s\n" +
            "License #: %s\n" +
            "Qualifications: %s\n" +
            "Status: Active",
            staff.id, staff.name, staff.role, staff.department,
            staff.specialty==null?"":staff.specialty, staff.phone==null?"":staff.phone,
            staff.email==null?"":staff.email, staff.licenseNumber==null?"":staff.licenseNumber,
            staff.qualifications==null?"":staff.qualifications
        );

        JOptionPane.showMessageDialog(this, details, "Staff Details - " + staffId, JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        doctorModel.setRowCount(0);
        nurseModel.setRowCount(0);
        cashierModel.setRowCount(0);

        int doctorCount = 0;
        int nurseCount = 0;
        int cashierCount = 0;

        for (Staff staff : DataStore.staff.values()) {
            String details = getStaffDetails(staff);
            Object[] row = new Object[]{
                staff.id,
                staff.name,
                staff.department,
                details,
                "Active",
                staff.createdAt==null?"":staff.createdAt.toLocalDate().toString()
            };

            if (staff.role == StaffRole.DOCTOR) {
                doctorModel.addRow(row);
                doctorCount++;
            } else if (staff.role == StaffRole.NURSE) {
                nurseModel.addRow(row);
                nurseCount++;
            } else if (staff.role == StaffRole.CASHIER) {
                cashierModel.addRow(row);
                cashierCount++;
            }
        }

        String stats = String.format("Total Staff: %d | Doctors: %d | Nurses: %d | Cashiers: %d",
            DataStore.staff.size(), doctorCount, nurseCount, cashierCount);
        statsLabel.setText(stats);
    }

    private String getStaffDetails(Staff staff) {
        if (staff.role == StaffRole.DOCTOR) {
            return (staff.specialty == null ? "" : staff.specialty) + " | License: " + (staff.licenseNumber == null ? "N/A" : staff.licenseNumber);
        } else if (staff.role == StaffRole.NURSE) {
            return "License: " + (staff.licenseNumber == null ? "N/A" : staff.licenseNumber);
        } else if (staff.role == StaffRole.CASHIER) {
            return "Billing & Payment Processing";
        } else {
            return "Administrator";
        }
    }
}
