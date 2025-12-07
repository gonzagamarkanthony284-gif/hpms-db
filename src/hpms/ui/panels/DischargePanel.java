package hpms.ui.panels;

import hpms.model.*;
import hpms.ui.components.Theme;
import hpms.util.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DischargePanel extends JPanel {
    private JTable dischargeTable;
    private DefaultTableModel tableModel;

    public DischargePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BACKGROUND);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Theme.BACKGROUND);
        JLabel titleLabel = new JLabel("Patient Discharge");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Theme.FOREGROUND);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Patient", "Doctor", "Room", "Diagnosis", "Date", "Follow-up"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        dischargeTable = new JTable(tableModel);
        dischargeTable.setFont(new Font("Arial", Font.PLAIN, 11));
        dischargeTable.setRowHeight(25);
        dischargeTable.getTableHeader().setBackground(Theme.PRIMARY);
        dischargeTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(dischargeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Theme.BACKGROUND);
        
        JButton dischargeBtn = new JButton("Discharge");
        dischargeBtn.setBackground(Theme.PRIMARY);
        dischargeBtn.setForeground(Color.WHITE);
        dischargeBtn.addActionListener(e -> showDischargeDialog());
        
        JButton followUpBtn = new JButton("Add Follow-up");
        followUpBtn.setBackground(Theme.PRIMARY);
        followUpBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(dischargeBtn);
        buttonPanel.add(followUpBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void showDischargeDialog() {
        JOptionPane.showMessageDialog(this, "Discharge form would go here");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Discharge d : DataStore.discharges.values()) {
            String followUp = d.followUpInstructions != null && !d.followUpInstructions.isEmpty() ? "Yes" : "No";
            tableModel.addRow(new Object[]{
                d.id, d.patientId, d.dischargedByDoctorId, d.roomId,
                d.primaryDiagnosis, d.dischargeDate, followUp
            });
        }
    }
}
