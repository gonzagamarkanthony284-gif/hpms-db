package hpms.ui;

import hpms.model.*;
import hpms.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.time.format.DateTimeFormatter;

public class PatientDashboardWindow extends JFrame {
    private Patient patient;
    private JPanel contentPanel;
    private JLabel profileTab, visitsTab, insuranceTab, requestsTab;

    public PatientDashboardWindow(Patient patient) {
        this.patient = patient;
        
        setTitle("HPMS Patient Portal - " + patient.name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Sidebar + Content
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.add(createSidebar(), BorderLayout.WEST);
        
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(createProfilePanel(), "profile");
        contentPanel.add(createVisitsPanel(), "visits");
        contentPanel.add(createInsurancePanel(), "insurance");
        contentPanel.add(createRequestsPanel(), "requests");
        
        bodyPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
        
        // Default to profile tab
        switchTab("profile");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("Patient Portal - Welcome, " + patient.name);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 20, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
        
        header.add(title, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        
        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 245, 250));
        sidebar.setBorder(new LineBorder(new Color(200, 200, 200), 1, false));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        profileTab = createTabButton("👤 My Profile", "profile");
        visitsTab = createTabButton("📋 My Visits", "visits");
        insuranceTab = createTabButton("💳 Insurance", "insurance");
        requestsTab = createTabButton("📝 Requests", "requests");
        
        sidebar.add(profileTab);
        sidebar.add(visitsTab);
        sidebar.add(insuranceTab);
        sidebar.add(requestsTab);
        sidebar.add(Box.createVerticalGlue());
        
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel patientIdLabel = new JLabel("Patient ID: " + patient.id);
        patientIdLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        patientIdLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(patientIdLabel);
        
        JLabel lastActiveLabel = new JLabel("Last Active: Just now");
        lastActiveLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        lastActiveLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(lastActiveLabel);
        
        sidebar.add(infoPanel);
        
        return sidebar;
    }

    private JLabel createTabButton(String text, String tabName) {
        JLabel tab = new JLabel(text);
        tab.setFont(new Font("Arial", Font.PLAIN, 13));
        tab.setForeground(new Color(60, 60, 60));
        tab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tab.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tab.setBackground(new Color(200, 220, 240));
                tab.setOpaque(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tab.setOpaque(false);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchTab(tabName);
            }
        });
        return tab;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("My Profile Information");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        // Identity Section
        panel.add(hpms.ui.components.DetailSection.createHeader("👤 Identity Information"));
        panel.add(createReadOnlyField("Name:", patient.name));
        panel.add(createReadOnlyField("Age:", String.valueOf(patient.age)));
        panel.add(createReadOnlyField("Gender:", patient.gender.name()));
        panel.add(createReadOnlyField("Contact:", patient.contact));
        panel.add(createReadOnlyField("Address:", patient.address));
        
        panel.add(Box.createVerticalStrut(20));
        
        // Medical Section
        panel.add(hpms.ui.components.DetailSection.createHeader("📋 Medical Information"));
        panel.add(createReadOnlyField("Allergies:", patient.allergies.isEmpty() ? "None reported" : patient.allergies));
        panel.add(createReadOnlyField("Current Medications:", patient.medications.isEmpty() ? "None reported" : patient.medications));
        panel.add(createReadOnlyField("Past Medical History:", patient.pastMedicalHistory.isEmpty() ? "None reported" : patient.pastMedicalHistory));
        // Vitals: Height, Weight, BMI (if available)
        panel.add(createReadOnlyField("Height:", patient.heightCm == null ? "N/A" : (String.format(Locale.US, "%.1f cm", patient.heightCm))));
        panel.add(createReadOnlyField("Weight:", patient.weightKg == null ? "N/A" : (String.format(Locale.US, "%.1f kg", patient.weightKg))));
        Double bmiVal = patient.getBmi();
        String bmiText = "N/A";
        Color bmiColor = new Color(50,50,50);
        if (bmiVal != null) {
            String bmiCategory;
            if (bmiVal < 18.5) { bmiCategory = "Underweight"; bmiColor = new Color(0x2E86C1); }
            else if (bmiVal < 25.0) { bmiCategory = "Normal"; bmiColor = new Color(0x27AE60); }
            else if (bmiVal < 30.0) { bmiCategory = "Overweight"; bmiColor = new Color(0xF39C12); }
            else { bmiCategory = "Obese"; bmiColor = new Color(0xE74C3C); }
            bmiText = String.format(Locale.US, "%.2f (%s)", bmiVal, bmiCategory);
        }
        panel.add(createReadOnlyField("BMI:", bmiText, bmiColor));
        
        panel.add(Box.createVerticalStrut(20));
        
        // Lifestyle Section
        panel.add(hpms.ui.components.DetailSection.createHeader("💪 Lifestyle"));
        panel.add(createReadOnlyField("Smoking Status:", patient.smokingStatus == null ? "Not provided" : patient.smokingStatus));
        panel.add(createReadOnlyField("Alcohol Use:", patient.alcoholUse == null ? "Not provided" : patient.alcoholUse));
        panel.add(createReadOnlyField("Occupation:", patient.occupation == null ? "Not provided" : patient.occupation));
        
        panel.add(Box.createVerticalStrut(20));
        
        // Insurance Section
        panel.add(hpms.ui.components.DetailSection.createHeader("💳 Insurance Information"));
        panel.add(createReadOnlyField("Insurance Provider:", patient.insuranceProvider == null ? "None" : patient.insuranceProvider));
        panel.add(createReadOnlyField("Insurance ID:", patient.insuranceId == null ? "N/A" : maskInsuranceId(patient.insuranceId)));
        panel.add(createReadOnlyField("Policy Holder:", patient.policyHolderName == null ? "N/A" : patient.policyHolderName));
        panel.add(createReadOnlyField("Policy Relationship:", patient.policyRelationship == null ? "N/A" : patient.policyRelationship));
        
        panel.add(Box.createVerticalGlue());
        
        // Request Update Button
        JButton requestUpdateBtn = new JButton("🔄 Request Information Update");
        requestUpdateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        requestUpdateBtn.setMaximumSize(new Dimension(300, 40));
        requestUpdateBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        requestUpdateBtn.setBackground(new Color(0, 102, 102));
        requestUpdateBtn.setForeground(Color.WHITE);
        requestUpdateBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        requestUpdateBtn.setFocusPainted(false);
        requestUpdateBtn.addActionListener(e -> {
            String reason = JOptionPane.showInputDialog(this, "Please describe what information needs to be updated:", "Request Update", JOptionPane.PLAIN_MESSAGE);
            if (reason != null && !reason.trim().isEmpty()) {
                LogManager.log("patient_update_request " + patient.id + ": " + reason);
                JOptionPane.showMessageDialog(this, "Update request submitted. Admin will review shortly.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(requestUpdateBtn);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createVisitsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("My Visits & Appointments");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        // Find appointments for this patient
        boolean hasAppointments = false;
        for (Appointment appt : DataStore.appointments.values()) {
            if (appt.patientId.equals(patient.id)) {
                panel.add(createAppointmentCard(appt));
                panel.add(Box.createVerticalStrut(10));
                hasAppointments = true;
            }
        }
        
        if (!hasAppointments) {
            JLabel noAppts = new JLabel("No appointments scheduled");
            noAppts.setFont(new Font("Arial", Font.ITALIC, 12));
            noAppts.setForeground(new Color(100, 100, 100));
            noAppts.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(noAppts);
        }
        
        panel.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createInsurancePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Insurance Verification");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        if (patient.insuranceProvider == null || patient.insuranceProvider.equals("None")) {
            JLabel noInsurance = new JLabel("ℹ No insurance information on file");
            noInsurance.setFont(new Font("Arial", Font.PLAIN, 12));
            noInsurance.setForeground(new Color(100, 100, 100));
            noInsurance.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(noInsurance);
        } else {
            // Show verification status
            JPanel verificationCard = new JPanel();
            verificationCard.setLayout(new BoxLayout(verificationCard, BoxLayout.Y_AXIS));
            verificationCard.setBackground(new Color(240, 250, 240));
            verificationCard.setBorder(new LineBorder(new Color(0, 150, 0), 2));
            verificationCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel verifiedLabel = new JLabel("✓ Insurance Verified");
            verifiedLabel.setFont(new Font("Arial", Font.BOLD, 14));
            verifiedLabel.setForeground(new Color(0, 150, 0));
            verificationCard.add(verifiedLabel);
            verificationCard.add(Box.createVerticalStrut(10));
            
            JLabel providerLabel = new JLabel("Provider: " + patient.insuranceProvider);
            providerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            verificationCard.add(providerLabel);
            
            JLabel idLabel = new JLabel("Policy ID: " + maskInsuranceId(patient.insuranceId));
            idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            verificationCard.add(idLabel);
            
            verificationCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            panel.add(verificationCard);
        }
        
        panel.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(panel);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("My Requests");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        JLabel noRequests = new JLabel("No pending requests");
        noRequests.setFont(new Font("Arial", Font.ITALIC, 12));
        noRequests.setForeground(new Color(100, 100, 100));
        noRequests.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(noRequests);
        
        panel.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(panel);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }



    private JPanel createReadOnlyField(String label, String value) {
        JPanel field = new JPanel(new BorderLayout(5, 0));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setOpaque(false);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 11));
        labelComponent.setPreferredSize(new Dimension(200, 30));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 11));
        valueComponent.setForeground(new Color(50, 50, 50));
        
        field.add(labelComponent, BorderLayout.WEST);
        field.add(valueComponent, BorderLayout.CENTER);
        
        return field;
    }

    // Overloaded helper to allow a custom value color (used for BMI category highlighting)
    private JPanel createReadOnlyField(String label, String value, Color valueColor) {
        JPanel field = new JPanel(new BorderLayout(5, 0));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setOpaque(false);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 11));
        labelComponent.setPreferredSize(new Dimension(200, 30));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 11));
        valueComponent.setForeground(valueColor == null ? new Color(50,50,50) : valueColor);
        
        field.add(labelComponent, BorderLayout.WEST);
        field.add(valueComponent, BorderLayout.CENTER);
        
        return field;
    }

    private JPanel createAppointmentCard(Appointment appt) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(240, 248, 255));
        card.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        
        JLabel dateLabel = new JLabel("📅 " + appt.dateTime.format(formatter));
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(dateLabel);
        
        Staff staff = DataStore.staff.get(appt.staffId);
        String staffName = staff != null ? staff.name : "Unknown";
        JLabel staffLabel = new JLabel("👨‍⚕️ Doctor: " + staffName);
        staffLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        card.add(staffLabel);

        if (staff == null) {
            JPanel warn = new JPanel(new BorderLayout()); warn.setOpaque(true); warn.setBackground(new Color(255, 245, 235)); warn.setBorder(new LineBorder(new Color(220, 150, 80), 1));
            JLabel msg = new JLabel("⚠ Doctor no longer available. Please choose another doctor."); msg.setFont(new Font("Arial", Font.BOLD, 11)); msg.setForeground(new Color(180, 90, 0)); warn.add(msg, BorderLayout.CENTER);
            JButton chooseBtn = new JButton("Choose Doctor"); chooseBtn.setBackground(new Color(255, 140, 0)); chooseBtn.setForeground(Color.WHITE); chooseBtn.setFocusPainted(false); chooseBtn.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
            chooseBtn.addActionListener(e -> openRescheduleDialog(appt));
            warn.add(chooseBtn, BorderLayout.EAST);
            warn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            card.add(Box.createVerticalStrut(6));
            card.add(warn);
        } else if (!staff.isAvailable) {
            JPanel warn = new JPanel(new BorderLayout()); warn.setOpaque(true); warn.setBackground(new Color(255, 245, 235)); warn.setBorder(new LineBorder(new Color(220, 150, 80), 1));
            JLabel msg = new JLabel("⚠ Doctor unavailable today. You may reschedule."); msg.setFont(new Font("Arial", Font.BOLD, 11)); msg.setForeground(new Color(180, 90, 0)); warn.add(msg, BorderLayout.CENTER);
            JButton chooseBtn = new JButton("Reschedule"); chooseBtn.setBackground(new Color(255, 140, 0)); chooseBtn.setForeground(Color.WHITE); chooseBtn.setFocusPainted(false); chooseBtn.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
            chooseBtn.addActionListener(e -> openRescheduleDialog(appt));
            warn.add(chooseBtn, BorderLayout.EAST);
            warn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            card.add(Box.createVerticalStrut(6));
            card.add(warn);
        }
        
        JLabel deptLabel = new JLabel("🏥 Department: " + appt.department);
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        card.add(deptLabel);
        
        return card;
    }

    private void openRescheduleDialog(Appointment appt) {
        JComboBox<String> doctorCombo = new JComboBox<>(DataStore.staff.values().stream().filter(s -> s.role == hpms.model.StaffRole.DOCTOR).map(s -> s.id + " - " + s.name).toArray(String[]::new));
        JSpinner dateSpinner = new JSpinner(new javax.swing.SpinnerDateModel()); JSpinner.DateEditor de = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"); dateSpinner.setEditor(de);
        JSpinner timeSpinner = new JSpinner(new javax.swing.SpinnerDateModel()); JSpinner.DateEditor te = new JSpinner.DateEditor(timeSpinner, "HH:mm"); timeSpinner.setEditor(te);
        int r = JOptionPane.showConfirmDialog(this, new Object[]{"New Doctor", doctorCombo, "Date", dateSpinner, "Time", timeSpinner}, "Reschedule Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String docId = String.valueOf(doctorCombo.getSelectedItem()).split(" - ")[0];
            java.util.Date d = (java.util.Date) dateSpinner.getValue(); java.util.Date t = (java.util.Date) timeSpinner.getValue();
            java.time.LocalDate date = d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            java.time.LocalTime time = t.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
            java.util.List<String> out = hpms.service.AppointmentService.schedule(appt.patientId, docId, date.toString(), time.toString(), appt.department);
            if (!out.isEmpty() && out.get(0).startsWith("Error:")) {
                JOptionPane.showMessageDialog(this, out.get(0), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                appt.isCompleted = true; appt.notes = (appt.notes==null?"":"[Rescheduled] ") + "Rescheduled due to doctor unavailability";
                try { hpms.util.BackupUtil.saveToDefault(); } catch (Exception ex) {}
                JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                // refresh appointments view by reconstructing panel
                getContentPane().repaint();
            }
        }
    }

    private String maskInsuranceId(String id) {
        if (id == null || id.length() < 4) return "****";
        return id.substring(0, 2) + "****" + id.substring(Math.max(0, id.length() - 2));
    }

    private void switchTab(String tabName) {
        // Reset all tabs
        profileTab.setOpaque(false);
        visitsTab.setOpaque(false);
        insuranceTab.setOpaque(false);
        requestsTab.setOpaque(false);
        
        // Highlight selected tab
        switch (tabName) {
            case "profile":
                profileTab.setOpaque(true);
                profileTab.setBackground(new Color(0, 102, 102));
                profileTab.setForeground(Color.WHITE);
                break;
            case "visits":
                visitsTab.setOpaque(true);
                visitsTab.setBackground(new Color(0, 102, 102));
                visitsTab.setForeground(Color.WHITE);
                break;
            case "insurance":
                insuranceTab.setOpaque(true);
                insuranceTab.setBackground(new Color(0, 102, 102));
                insuranceTab.setForeground(Color.WHITE);
                break;
            case "requests":
                requestsTab.setOpaque(true);
                requestsTab.setBackground(new Color(0, 102, 102));
                requestsTab.setForeground(Color.WHITE);
                break;
        }
        
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, tabName);
    }
}
