package hpms.ui.panels;

import hpms.model.Appointment;
import hpms.model.Staff;
import hpms.ui.components.Theme;
import hpms.util.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class DoctorSchedulePanel extends JPanel {
    private JComboBox<String> doctorCombo;
    private DefaultTableModel todayModel, upcomingModel, pendingModel;
    private JTable todayTable, upcomingTable, pendingTable;
    private JLabel todayCount, upcomingCount, pendingCount;

    public DoctorSchedulePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with doctor selector
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Three-section main content
        JPanel mainContent = createMainContentPanel();
        add(mainContent, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        refreshAllTables();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(0, 0, 10, 0)
        ));

        JLabel titleLabel = new JLabel("Doctor Schedule & Appointments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Theme.FOREGROUND);
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        selectorPanel.setBackground(Theme.BACKGROUND);
        JLabel doctorLabel = new JLabel("Doctor:");
        doctorLabel.setForeground(Theme.FOREGROUND);
        doctorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        doctorCombo = new JComboBox<>();
        doctorCombo.addItem("-- All Doctors --");
        for (Staff staff : DataStore.staff.values()) {
            if (staff.role.name().equalsIgnoreCase("DOCTOR")) {
                doctorCombo.addItem(staff.id + " - " + staff.name);
            }
        }
        doctorCombo.setPreferredSize(new Dimension(250, 28));
        Theme.styleTextField(new JTextField());
        doctorCombo.setBackground(Color.WHITE);
        doctorCombo.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        doctorCombo.addActionListener(e -> refreshAllTables());

        selectorPanel.add(doctorLabel);
        selectorPanel.add(doctorCombo);
        panel.add(selectorPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(Theme.BACKGROUND);

        panel.add(createSectionPanel("Today's Schedule", createTodayTable(), todayCount = new JLabel("0 appointments")));
        panel.add(createSectionPanel("Upcoming Schedule (Next 7 Days)", createUpcomingTable(), upcomingCount = new JLabel("0 appointments")));
        panel.add(createSectionPanel("Pending Appointments (Awaiting Doctor Acceptance)", createPendingTable(), pendingCount = new JLabel("0 pending")));

        return panel;
    }

    private JPanel createSectionPanel(String title, JScrollPane table, JLabel countLabel) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(Theme.PRIMARY);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        countLabel.setForeground(new Color(100, 100, 100));
        titlePanel.add(countLabel, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane createTodayTable() {
        String[] columns = {"Patient", "Doctor", "Time", "Department", "Notes", "Status"};
        todayModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        todayTable = new JTable(todayModel);
        todayTable.setFont(new Font("Arial", Font.PLAIN, 11));
        todayTable.setRowHeight(24);
        todayTable.getTableHeader().setBackground(new Color(220, 240, 255));
        todayTable.getTableHeader().setForeground(Theme.PRIMARY);
        todayTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(todayTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return scrollPane;
    }

    private JScrollPane createUpcomingTable() {
        String[] columns = {"Date", "Patient", "Doctor", "Time", "Department", "Status"};
        upcomingModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        upcomingTable = new JTable(upcomingModel);
        upcomingTable.setFont(new Font("Arial", Font.PLAIN, 11));
        upcomingTable.setRowHeight(24);
        upcomingTable.getTableHeader().setBackground(new Color(220, 240, 255));
        upcomingTable.getTableHeader().setForeground(Theme.PRIMARY);
        upcomingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(upcomingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return scrollPane;
    }

    private JScrollPane createPendingTable() {
        String[] columns = {"Patient", "Doctor", "Requested Date/Time", "Department", "Reason", "Status"};
        pendingModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pendingTable = new JTable(pendingModel);
        pendingTable.setFont(new Font("Arial", Font.PLAIN, 11));
        pendingTable.setRowHeight(24);
        pendingTable.getTableHeader().setBackground(new Color(255, 240, 220));
        pendingTable.getTableHeader().setForeground(new Color(200, 100, 0));
        pendingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(pendingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return scrollPane;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JButton acceptBtn = createStyledButton("Accept Appointment", new Color(76, 175, 80));
        acceptBtn.addActionListener(e -> acceptPendingAppointment());

        JButton rejectBtn = createStyledButton("Reject Appointment", new Color(244, 67, 54));
        rejectBtn.addActionListener(e -> rejectPendingAppointment());

        JButton setFreeScheduleBtn = createStyledButton("Set Free Schedule", Theme.PRIMARY);
        setFreeScheduleBtn.addActionListener(e -> showSetFreeScheduleDialog());

        JButton viewDetailsBtn = createStyledButton("View Details", new Color(33, 150, 243));
        viewDetailsBtn.addActionListener(e -> viewAppointmentDetails());

        JButton refreshBtn = createStyledButton("Refresh", new Color(155, 155, 155));
        refreshBtn.addActionListener(e -> refreshAllTables());

        panel.add(acceptBtn);
        panel.add(rejectBtn);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(setFreeScheduleBtn);
        panel.add(viewDetailsBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshAllTables() {
        refreshTodayTable();
        refreshUpcomingTable();
        refreshPendingTable();
    }

    private void refreshTodayTable() {
        todayModel.setRowCount(0);
        LocalDate today = LocalDate.now();
        int count = 0;

        String selectedDoctorId = getSelectedDoctorId();
        for (Appointment appt : DataStore.appointments.values()) {
            if (appt.dateTime.toLocalDate().equals(today)) {
                if (selectedDoctorId == null || appt.staffId.equals(selectedDoctorId)) {
                    Staff doctor = DataStore.staff.get(appt.staffId);
                    String doctorName = doctor != null ? doctor.name : appt.staffId;
                    todayModel.addRow(new Object[]{
                            appt.patientId,
                            doctorName,
                            appt.dateTime.toLocalTime(),
                            appt.department,
                            appt.notes.length() > 30 ? appt.notes.substring(0, 30) + "..." : appt.notes,
                            appt.isCompleted ? "Completed" : "Scheduled"
                    });
                    count++;
                }
            }
        }
        todayCount.setText(count + " appointments");
    }

    private void refreshUpcomingTable() {
        upcomingModel.setRowCount(0);
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        int count = 0;

        String selectedDoctorId = getSelectedDoctorId();
        for (Appointment appt : DataStore.appointments.values()) {
            LocalDate apptDate = appt.dateTime.toLocalDate();
            if (apptDate.isAfter(today) && apptDate.isBefore(nextWeek)) {
                if (selectedDoctorId == null || appt.staffId.equals(selectedDoctorId)) {
                    Staff doctor = DataStore.staff.get(appt.staffId);
                    String doctorName = doctor != null ? doctor.name : appt.staffId;
                    upcomingModel.addRow(new Object[]{
                            apptDate,
                            appt.patientId,
                            doctorName,
                            appt.dateTime.toLocalTime(),
                            appt.department,
                            appt.isCompleted ? "Completed" : "Scheduled"
                    });
                    count++;
                }
            }
        }
        upcomingCount.setText(count + " appointments");
    }

    private void refreshPendingTable() {
        pendingModel.setRowCount(0);
        int count = 0;

        String selectedDoctorId = getSelectedDoctorId();
        for (Appointment appt : DataStore.appointments.values()) {
            if (!appt.isCompleted && appt.notes.toLowerCase().contains("pending")) {
                if (selectedDoctorId == null || appt.staffId.equals(selectedDoctorId)) {
                    Staff doctor = DataStore.staff.get(appt.staffId);
                    String doctorName = doctor != null ? doctor.name : appt.staffId;
                    pendingModel.addRow(new Object[]{
                            appt.patientId,
                            doctorName,
                            appt.dateTime,
                            appt.department,
                            appt.notes,
                            "Pending Acceptance"
                    });
                    count++;
                }
            }
        }
        pendingCount.setText(count + " pending");
    }

    private String getSelectedDoctorId() {
        if (doctorCombo.getSelectedIndex() <= 0) return null;
        String selected = (String) doctorCombo.getSelectedItem();
        return selected.split(" - ")[0];
    }

    private void acceptPendingAppointment() {
        int row = pendingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to accept", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String patientId = (String) pendingModel.getValueAt(row, 0);
        java.time.LocalDateTime dateTime = (java.time.LocalDateTime) pendingModel.getValueAt(row, 2);
        String dept = String.valueOf(pendingModel.getValueAt(row, 3));
        hpms.model.Appointment target = null;
        for (hpms.model.Appointment a : hpms.util.DataStore.appointments.values()) {
            if (patientId.equals(a.patientId) && a.dateTime.equals(dateTime) && dept.equals(a.department)) { target = a; break; }
        }
        if (target == null) {
            JOptionPane.showMessageDialog(this, "Could not locate appointment to accept", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (target.notes != null) target.notes = target.notes.replaceAll("(?i)pending", "").trim();
        if (target.notes == null || target.notes.isEmpty()) target.notes = "Accepted";
        try { hpms.util.BackupUtil.saveToDefault(); } catch (Exception ex) { }
        JOptionPane.showMessageDialog(this, "Appointment for patient " + patientId + " accepted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        refreshAllTables();
    }

    private void rejectPendingAppointment() {
        int row = pendingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to reject", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String patientId = (String) pendingModel.getValueAt(row, 0);
        java.time.LocalDateTime dateTime = (java.time.LocalDateTime) pendingModel.getValueAt(row, 2);
        String dept = String.valueOf(pendingModel.getValueAt(row, 3));
        String reason = JOptionPane.showInputDialog(this, "Reason for rejection:", "");
        if (reason == null) return;
        hpms.model.Appointment target = null;
        for (hpms.model.Appointment a : hpms.util.DataStore.appointments.values()) {
            if (patientId.equals(a.patientId) && a.dateTime.equals(dateTime) && dept.equals(a.department)) { target = a; break; }
        }
        if (target == null) {
            JOptionPane.showMessageDialog(this, "Could not locate appointment to reject", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        java.util.List<String> out = hpms.service.AppointmentService.cancel(target.id);
        if (!out.isEmpty() && out.get(0).startsWith("Error:")) {
            JOptionPane.showMessageDialog(this, out.get(0), "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Appointment for patient " + patientId + " rejected.\nReason: " + reason, "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAllTables();
        }
    }

    private void showSetFreeScheduleDialog() {
        String doctorId = getSelectedDoctorId();
        if (doctorId == null) {
            JOptionPane.showMessageDialog(this, "Please select a doctor first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("Set Free Schedule");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Date selector
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        panel.add(dateSpinner, gbc);

        // Time selectors
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Start Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        JTextField startTimeField = new JTextField("09:00", 15);
        Theme.styleTextField(startTimeField);
        panel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("End Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        JTextField endTimeField = new JTextField("17:00", 15);
        Theme.styleTextField(endTimeField);
        panel.add(endTimeField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Free schedule saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            refreshAllTables();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void viewAppointmentDetails() {
        JTable selectedTable = null;
        int row = -1;

        if (todayTable.getSelectedRow() >= 0) {
            selectedTable = todayTable;
            row = todayTable.getSelectedRow();
        } else if (upcomingTable.getSelectedRow() >= 0) {
            selectedTable = upcomingTable;
            row = upcomingTable.getSelectedRow();
        } else if (pendingTable.getSelectedRow() >= 0) {
            selectedTable = pendingTable;
            row = pendingTable.getSelectedRow();
        }

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to view details", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String patientId = (String) selectedTable.getValueAt(row, selectedTable == todayTable ? 0 : (selectedTable == upcomingTable ? 1 : 0));
        String details = "Appointment Details:\n\nPatient ID: " + patientId + "\nDetailed information coming soon...";
        JOptionPane.showMessageDialog(this, details, "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
