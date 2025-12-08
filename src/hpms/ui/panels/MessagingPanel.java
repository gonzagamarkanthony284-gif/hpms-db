package hpms.ui.panels;

import hpms.model.Communication;
import hpms.model.Staff;
import hpms.model.Patient;
import hpms.ui.components.Theme;
import hpms.util.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MessagingPanel extends JPanel {
    private JTable messageTable;
    private DefaultTableModel tableModel;

    public MessagingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BACKGROUND);

        JLabel titleLabel = new JLabel("Messaging");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Theme.FOREGROUND);
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = { "Status", "From", "Subject", "Date" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        messageTable = new JTable(tableModel);
        messageTable.setFont(new Font("Arial", Font.PLAIN, 11));
        messageTable.setRowHeight(25);
        messageTable.getTableHeader().setBackground(Theme.PRIMARY);
        messageTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(messageTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Theme.BACKGROUND);

        JButton sendBtn = new JButton("Send");
        sendBtn.setBackground(Theme.PRIMARY);
        sendBtn.setForeground(Color.WHITE);
        buttonPanel.add(sendBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Communication c : DataStore.communications.values()) {
            String status = c.isRead ? "[Read]" : "[NEW]";
            // Get sender's name - could be staff (doctor) or patient
            String senderName = c.senderId;
            if (c.senderId.startsWith("S")) {
                // Staff member (doctor, nurse, etc.)
                Staff staff = DataStore.staff.get(c.senderId);
                if (staff != null && staff.name != null) {
                    senderName = staff.name;
                }
            } else if (c.senderId.startsWith("P")) {
                // Patient
                Patient patient = DataStore.patients.get(c.senderId);
                if (patient != null && patient.name != null) {
                    senderName = patient.name;
                }
            }
            tableModel.addRow(new Object[] { status, senderName, c.subject, c.sentDate });
        }
    }
}
