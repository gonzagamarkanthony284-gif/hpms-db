package hpms.ui.panels;

import hpms.model.*;
import hpms.util.*;
import hpms.ui.components.SectionHeader;
import hpms.ui.components.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private DefaultTableModel appointmentModel, activityModel, billingModel;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(SectionHeader.info("Reports & Analytics", "View system reports, statistics, and activity logs"), BorderLayout.NORTH);

        // Tabbed interface
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Theme.BG);

        // Tab 1: Appointment Report
        JPanel appointmentTab = createAppointmentTab();
        tabbedPane.addTab("Appointments", appointmentTab);

        // Tab 2: Billing Report
        JPanel billingTab = createBillingTab();
        tabbedPane.addTab("Billing", billingTab);

        // Tab 3: Activity Log
        JPanel activityTab = createActivityTab();
        tabbedPane.addTab("Activity Log", activityTab);

        // Tab 4: Patient Statistics
        JPanel statsTab = createStatsTab();
        tabbedPane.addTab("Statistics", statsTab);

        add(tabbedPane, BorderLayout.CENTER);
        refresh();

        // Refresh reports tab when shown again
        this.addHierarchyListener(evt -> {
            if ((evt.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (this.isShowing()) SwingUtilities.invokeLater(this::refresh);
            }
        });
    }

    private JPanel createAppointmentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Stats
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 8));
        statsPanel.setBackground(Theme.BG);

        JLabel totalLabel = new JLabel("Total: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel upcomingLabel = new JLabel("Upcoming: 0");
        upcomingLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        upcomingLabel.setForeground(new Color(0, 110, 102));
        JLabel completedLabel = new JLabel("Completed: 0");
        completedLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedLabel.setForeground(new Color(52, 152, 219));

        statsPanel.add(totalLabel);
        statsPanel.add(upcomingLabel);
        statsPanel.add(completedLabel);

        panel.add(statsPanel, BorderLayout.NORTH);

        // Table
        appointmentModel = new DefaultTableModel(
            new String[]{"ID", "Patient", "Doctor", "Date", "Time", "Status"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(appointmentModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actionPanel.setBackground(Theme.BG);

        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, new Color(0, 110, 102));
        refreshBtn.addActionListener(e -> {
            appointmentModel.setRowCount(0);
            int total = 0, upcoming = 0, completed = 0;

            for (Appointment apt : DataStore.appointments.values()) {
                Patient p = DataStore.patients.get(apt.patientId);
                Staff s = DataStore.staff.get(apt.staffId);
                String patientName = p != null ? p.name : "Unknown";
                String staffName = s != null ? s.name : "Unknown";
                
                appointmentModel.addRow(new Object[]{
                    apt.id,
                    patientName,
                    staffName,
                    apt.dateTime.toLocalDate(),
                    apt.dateTime.toLocalTime(),
                    "Scheduled"
                });
                total++;
                upcoming++;
            }

            totalLabel.setText("Total: " + total);
            upcomingLabel.setText("Upcoming: " + upcoming);
            completedLabel.setText("Completed: " + completed);
        });

        JButton exportBtn = new JButton("Export to CSV");
        styleButton(exportBtn, new Color(41, 128, 185));
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Export feature: Save report as CSV file", "Export", JOptionPane.INFORMATION_MESSAGE));

        actionPanel.add(refreshBtn);
        actionPanel.add(exportBtn);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBillingTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Stats
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 8));
        statsPanel.setBackground(Theme.BG);

        JLabel totalRevenueLabel = new JLabel("Total Revenue: $0");
        totalRevenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalRevenueLabel.setForeground(new Color(46, 204, 113));

        JLabel paidLabel = new JLabel("Paid: $0");
        paidLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paidLabel.setForeground(new Color(52, 152, 219));

        JLabel unpaidLabel = new JLabel("Unpaid: $0");
        unpaidLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        unpaidLabel.setForeground(new Color(192, 57, 43));

        statsPanel.add(totalRevenueLabel);
        statsPanel.add(paidLabel);
        statsPanel.add(unpaidLabel);

        panel.add(statsPanel, BorderLayout.NORTH);

        // Table
        billingModel = new DefaultTableModel(
            new String[]{"Bill ID", "Patient", "Amount", "Paid", "Status", "Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(billingModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actionPanel.setBackground(Theme.BG);

        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, new Color(0, 110, 102));
        refreshBtn.addActionListener(e -> {
            billingModel.setRowCount(0);
            double totalRevenue = 0, paid = 0, unpaid = 0;

            for (Bill bill : DataStore.bills.values()) {
                Patient p = DataStore.patients.get(bill.patientId);
                String patientName = p != null ? p.name : "Unknown";
                
                billingModel.addRow(new Object[]{
                    bill.id,
                    patientName,
                    "$" + String.format("%.2f", bill.total),
                    bill.paid ? "$" + String.format("%.2f", bill.total) : "$0.00",
                    bill.paid ? "PAID" : "PENDING",
                    "2024-01-01"
                });
                totalRevenue += bill.total;
                paid += bill.paid ? bill.total : 0;
                unpaid += bill.paid ? 0 : bill.total;
            }

            totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", totalRevenue));
            paidLabel.setText(String.format("Paid: $%.2f", paid));
            unpaidLabel.setText(String.format("Unpaid: $%.2f", unpaid));
        });

        actionPanel.add(refreshBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createActivityTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        activityModel = new DefaultTableModel(
            new String[]{"Timestamp", "User", "Action", "Details"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(activityModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);

        // Sample activity log entries
        activityModel.addRow(new Object[]{"2024-01-20 14:30", "admin", "LOGIN", "User logged in"});
        activityModel.addRow(new Object[]{"2024-01-20 14:35", "admin", "PATIENT_ADD", "Added patient: John Doe"});
        activityModel.addRow(new Object[]{"2024-01-20 14:45", "doctor1", "APPOINTMENT_SCHEDULE", "Scheduled appointment"});
        activityModel.addRow(new Object[]{"2024-01-20 15:00", "nurse1", "BILL_CREATE", "Created billing record"});
        activityModel.addRow(new Object[]{"2024-01-20 15:15", "admin", "BACKUP", "System backup completed"});

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actionPanel.setBackground(Theme.BG);

        JButton clearBtn = new JButton("Clear Log");
        styleButton(clearBtn, new Color(192, 57, 43));
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear all activity logs?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                activityModel.setRowCount(0);
                JOptionPane.showMessageDialog(this, "Activity log cleared", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        actionPanel.add(clearBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStatsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 16, 16));
        statsPanel.setBackground(Theme.BG);

        // Stat cards
        statsPanel.add(createStatCard("Total Patients", String.valueOf(DataStore.patients.size()), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Appointments", String.valueOf(DataStore.appointments.size()), new Color(0, 110, 102)));
        statsPanel.add(createStatCard("Total Staff", String.valueOf(DataStore.staff.size()), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Rooms", String.valueOf(DataStore.rooms.size()), new Color(230, 126, 34)));

        panel.add(statsPanel, BorderLayout.NORTH);

        // Details text area
        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setFont(new Font("Courier New", Font.PLAIN, 11));
        details.setText(generateStatistics());
        details.setBorder(new EmptyBorder(12, 12, 12, 12));

        panel.add(new JScrollPane(details), BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actionPanel.setBackground(Theme.BG);

        JButton refreshBtn = new JButton("Refresh Stats");
        styleButton(refreshBtn, new Color(0, 110, 102));
        refreshBtn.addActionListener(e -> details.setText(generateStatistics()));

        actionPanel.add(refreshBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(new LineBorder(color, 3));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setBorder(new EmptyBorder(8, 12, 0, 12));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setBorder(new EmptyBorder(12, 12, 12, 12));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private String generateStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SYSTEM STATISTICS ===\n\n");

        sb.append("PATIENTS:\n");
        sb.append("  Total Count: ").append(DataStore.patients.size()).append("\n");
        int activePatients = DataStore.patients.size();  // All patients are active by default
        sb.append("  Active: ").append(activePatients).append("\n");
        sb.append("  Discharged: 0\n\n");

        sb.append("APPOINTMENTS:\n");
        sb.append("  Total: ").append(DataStore.appointments.size()).append("\n\n");

        sb.append("BILLING:\n");
        double totalBilled = 0, totalPaid = 0;
        for (Bill b : DataStore.bills.values()) {
            totalBilled += b.total;
            totalPaid += b.paid ? b.total : 0;
        }
        sb.append("  Total Bills: ").append(DataStore.bills.size()).append("\n");
        sb.append("  Total Billed: $").append(String.format("%.2f", totalBilled)).append("\n");
        sb.append("  Total Paid: $").append(String.format("%.2f", totalPaid)).append("\n");
        sb.append("  Pending: $").append(String.format("%.2f", totalBilled - totalPaid)).append("\n\n");

        sb.append("ROOMS:\n");
        int occupied = 0;
        for (Room r : DataStore.rooms.values()) {
            if (r.occupantPatientId != null && !r.occupantPatientId.isEmpty()) occupied++;
        }
        sb.append("  Total Rooms: ").append(DataStore.rooms.size()).append("\n");
        sb.append("  Occupied: ").append(occupied).append("\n");
        sb.append("  Available: ").append(DataStore.rooms.size() - occupied).append("\n\n");

        sb.append("STAFF:\n");
        sb.append("  Total: ").append(DataStore.staff.size()).append("\n");

        return sb.toString();
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void refresh() {
        // Refresh all tabs when panel is shown
    }
}

