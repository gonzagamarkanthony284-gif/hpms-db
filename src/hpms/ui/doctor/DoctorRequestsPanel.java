package hpms.ui.doctor;

import hpms.auth.AuthSession;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Doctor Requests Panel - manage appointment requests
 */
public class DoctorRequestsPanel extends JPanel {
    private JTable requestsTable;

    public DoctorRequestsPanel(AuthSession session) {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("Appointment Requests");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(31, 41, 55));
        add(title, BorderLayout.NORTH);

        // Table
        requestsTable = new JTable();
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.setRowHeight(28);

        String[] columns = {"Request ID", "Patient Name", "Requested Date", "Reason", "Status", "Action"};
        Object[][] data = {};

        requestsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });

        JScrollPane scroll = new JScrollPane(requestsTable);
        scroll.setBorder(new LineBorder(new Color(226, 232, 240)));
        add(scroll, BorderLayout.CENTER);

        // Bottom action panel
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        actions.setBackground(new Color(248, 249, 250));
        actions.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        JButton acceptBtn = new JButton("Accept Request");
        acceptBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        acceptBtn.setBackground(new Color(34, 197, 94));
        acceptBtn.setForeground(Color.WHITE);
        acceptBtn.setFocusPainted(false);

        JButton rejectBtn = new JButton("Reject Request");
        rejectBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        rejectBtn.setBackground(new Color(239, 68, 68));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFocusPainted(false);

        actions.add(acceptBtn);
        actions.add(rejectBtn);
        add(actions, BorderLayout.SOUTH);
    }

    public void refresh() {
        // Refresh requests from DataStore
    }
}
