package hpms.ui.panels;

import hpms.model.*;
import hpms.service.*;
import hpms.util.*;
import hpms.ui.components.SectionHeader;
import hpms.ui.components.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LaboratoryPanel extends JPanel {
    private DefaultTableModel testRequestModel;
    private DefaultTableModel testResultModel;
    private JTabbedPane tabbedPane;
    private JLabel statsLabel;

    public LaboratoryPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(SectionHeader.info("Laboratory Management", "Request, process and track lab tests and results"), BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Tabbed interface
        tabbedPane = new JTabbedPane();

        // Test Requests tab
        testRequestModel = new DefaultTableModel(
            new String[]{"Request ID", "Patient", "Doctor", "Test Name", "Status", "Requested Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable testReqTable = new JTable(testRequestModel);
        testReqTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane reqScrollPane = new JScrollPane(testReqTable);

        JPanel requestPanel = new JPanel(new BorderLayout());
        requestPanel.add(reqScrollPane, BorderLayout.CENTER);
        JPanel requestActionPanel = createRequestActionPanel(testReqTable);
        requestPanel.add(requestActionPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Test Requests", requestPanel);

        // Test Results tab
        testResultModel = new DefaultTableModel(
            new String[]{"Result ID", "Request ID", "Patient", "Test Name", "Result Status", "Result Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable testResultTable = new JTable(testResultModel);
        testResultTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane resultScrollPane = new JScrollPane(testResultTable);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);
        JPanel resultActionPanel = createResultActionPanel(testResultTable);
        resultPanel.add(resultActionPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Test Results", resultPanel);

        add(tabbedPane, BorderLayout.CENTER);

        refresh();

        // refresh when tab becomes visible to avoid stale/blank data after navigation
        this.addHierarchyListener(evt -> {
            if ((evt.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (this.isShowing()) SwingUtilities.invokeLater(this::refresh);
            }
        });
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));

        statsLabel = new JLabel("Total Requests: 0 | Pending: 0 | Completed: 0 | Results: 0");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(new Color(80, 80, 80));

        panel.add(statsLabel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createRequestActionPanel(JTable table) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(8, 12, 12, 12));

        JButton requestBtn = new JButton("Request Test");
        styleButton(requestBtn, new Color(0, 110, 102));

        JButton processBtn = new JButton("Mark as Processed");
        styleButton(processBtn, new Color(39, 174, 96));

        JButton viewBtn = new JButton("View Details");
        styleButton(viewBtn, new Color(155, 89, 182));

        requestBtn.addActionListener(e -> requestLabTestDialog());
        processBtn.addActionListener(e -> markAsProcessed(table));
        viewBtn.addActionListener(e -> viewRequestDetails(table));

        panel.add(requestBtn);
        panel.add(processBtn);
        panel.add(viewBtn);

        return panel;
    }

    private JPanel createResultActionPanel(JTable table) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(8, 12, 12, 12));

        JButton enterBtn = new JButton("Enter Result");
        styleButton(enterBtn, new Color(0, 110, 102));

        JButton viewBtn = new JButton("View Result");
        styleButton(viewBtn, new Color(41, 128, 185));

        JButton printBtn = new JButton("Print Report");
        styleButton(printBtn, new Color(155, 89, 182));

        enterBtn.addActionListener(e -> enterLabResultDialog(table));
        viewBtn.addActionListener(e -> viewResultDetails(table));
        printBtn.addActionListener(e -> printLabReport(table));

        panel.add(enterBtn);
        panel.add(viewBtn);
        panel.add(printBtn);

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

    private void requestLabTestDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Request Lab Test", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setBackground(new Color(245, 247, 250));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0.3;
        panel.add(new JLabel("Patient *"), c);
        c.gridx = 1; c.weightx = 0.7;
        JComboBox<String> patientCombo = new JComboBox<>();
        DataStore.patients.forEach((id, p) -> patientCombo.addItem(id + " - " + p.name));
        panel.add(patientCombo, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0.3;
        panel.add(new JLabel("Doctor *"), c);
        c.gridx = 1; c.weightx = 0.7;
        JComboBox<String> doctorCombo = new JComboBox<>();
        DataStore.staff.forEach((id, s) -> {
            if (s.role == StaffRole.DOCTOR) doctorCombo.addItem(id + " - " + s.name);
        });
        panel.add(doctorCombo, c);

        c.gridx = 0; c.gridy = 2; c.weightx = 0.3;
        panel.add(new JLabel("Test Name *"), c);
        c.gridx = 1; c.weightx = 0.7;
        JComboBox<String> testCombo = new JComboBox<>(new String[]{"CBC", "Blood Glucose", "Cholesterol", "Kidney Function", "Liver Function", "Thyroid Test", "Pregnancy Test", "Other"});
        JTextField testOther = new JTextField();
        testOther.setVisible(false);
        panel.add(testCombo, c);

        testCombo.addActionListener(e -> {
            boolean isOther = "Other".equals(testCombo.getSelectedItem().toString());
            testOther.setVisible(isOther);
        });

        c.gridx = 0; c.gridy = 3; c.weightx = 0.3;
        panel.add(new JLabel("If Other, specify"), c);
        c.gridx = 1; c.weightx = 0.7;
        panel.add(testOther, c);

        c.gridx = 0; c.gridy = 4; c.weightx = 0.3;
        panel.add(new JLabel("Urgency"), c);
        c.gridx = 1; c.weightx = 0.7;
        JComboBox<String> urgency = new JComboBox<>(new String[]{"Routine", "Urgent", "STAT"});
        panel.add(urgency, c);

        c.gridx = 0; c.gridy = 5; c.weightx = 0.3;
        panel.add(new JLabel("Notes"), c);
        c.gridx = 1; c.weightx = 0.7; c.weighty = 1.0; c.fill = GridBagConstraints.BOTH;
        JTextArea notesArea = new JTextArea(3, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(notesArea), c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton saveBtn = new JButton("Request");
        styleButton(saveBtn, new Color(0, 110, 102));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            if (patientCombo.getSelectedIndex() < 0 || doctorCombo.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String patientId = patientCombo.getSelectedItem().toString().split(" - ")[0];
            String doctorId = doctorCombo.getSelectedItem().toString().split(" - ")[0];
            String testName = "Other".equals(testCombo.getSelectedItem().toString()) ? testOther.getText() : testCombo.getSelectedItem().toString();

            java.util.List<String> result = LabService.request(patientId, doctorId, testName);
            if (result.get(0).startsWith("Lab test requested")) {
                JOptionPane.showMessageDialog(dialog, "Test request created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    private void enterLabResultDialog(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a test request to enter results", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String requestId = testResultModel.getValueAt(row, 1).toString();

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Enter Lab Result", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0.2;
        panel.add(new JLabel("Request ID"), c);
        c.gridx = 1; c.weightx = 0.8;
        JTextField requestIdField = new JTextField(requestId);
        requestIdField.setEditable(false);
        panel.add(requestIdField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0.2;
        panel.add(new JLabel("Result Text *"), c);
        c.gridx = 1; c.weightx = 0.8; c.weighty = 1.0; c.fill = GridBagConstraints.BOTH;
        JTextArea resultArea = new JTextArea(8, 40);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(resultArea), c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton saveBtn = new JButton("Enter Result");
        styleButton(saveBtn, new Color(0, 110, 102));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            if (resultArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter result text", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.List<String> result = LabService.enterResult(requestId, resultArea.getText());
            if (result.get(0).startsWith("Lab result")) {
                JOptionPane.showMessageDialog(dialog, "Result entered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    private void markAsProcessed(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a test request", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Test marked as processed", "Success", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }

    private void viewRequestDetails(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a test request", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String details = "Request ID: " + testRequestModel.getValueAt(row, 0) + "\n" +
                        "Patient: " + testRequestModel.getValueAt(row, 1) + "\n" +
                        "Doctor: " + testRequestModel.getValueAt(row, 2) + "\n" +
                        "Test: " + testRequestModel.getValueAt(row, 3) + "\n" +
                        "Status: " + testRequestModel.getValueAt(row, 4) + "\n" +
                        "Requested: " + testRequestModel.getValueAt(row, 5);

        JOptionPane.showMessageDialog(this, details, "Test Request Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewResultDetails(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a result", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String details = "Result ID: " + testResultModel.getValueAt(row, 0) + "\n" +
                        "Request ID: " + testResultModel.getValueAt(row, 1) + "\n" +
                        "Patient: " + testResultModel.getValueAt(row, 2) + "\n" +
                        "Test: " + testResultModel.getValueAt(row, 3) + "\n" +
                        "Status: " + testResultModel.getValueAt(row, 4) + "\n" +
                        "Date: " + testResultModel.getValueAt(row, 5);

        JOptionPane.showMessageDialog(this, details, "Lab Result Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void printLabReport(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a result to print", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String report = "=== LAB REPORT ===\n" +
                       "Result ID: " + testResultModel.getValueAt(row, 0) + "\n" +
                       "Patient: " + testResultModel.getValueAt(row, 2) + "\n" +
                       "Test: " + testResultModel.getValueAt(row, 3) + "\n" +
                       "Date: " + testResultModel.getValueAt(row, 5) + "\n" +
                       "Status: " + testResultModel.getValueAt(row, 4);

        JTextArea reportArea = new JTextArea(report, 10, 50);
        reportArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(reportArea), "Print Lab Report", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        testRequestModel.setRowCount(0);
        testResultModel.setRowCount(0);

        int pending = 0;
        int completed = 0;

        for (LabTestRequest req : DataStore.labTests.values()) {
            Patient patient = DataStore.patients.get(req.patientId);
            Staff doctor = DataStore.staff.get(req.doctorId);

            String patientName = patient != null ? patient.name : req.patientId;
            String doctorName = doctor != null ? doctor.name : req.doctorId;

            testRequestModel.addRow(new Object[]{
                req.id,
                patientName,
                doctorName,
                req.testName,
                "Pending",
                req.requestedAt
            });

            pending++;
        }

        for (LabResult res : DataStore.labResults.values()) {
            LabTestRequest req = DataStore.labTests.get(res.testRequestId);
            Patient patient = req != null ? DataStore.patients.get(req.patientId) : null;

            String patientName = patient != null ? patient.name : "N/A";
            String testName = req != null ? req.testName : "N/A";

            testResultModel.addRow(new Object[]{
                res.id,
                res.testRequestId,
                patientName,
                testName,
                "Completed",
                res.enteredAt
            });

            completed++;
        }

        String stats = String.format("Total Requests: %d | Pending: %d | Completed: %d | Results: %d",
            DataStore.labTests.size(), pending, completed, completed);
        statsLabel.setText(stats);
    }
}

