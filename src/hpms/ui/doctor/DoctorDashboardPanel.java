package hpms.ui.doctor;

import hpms.auth.AuthSession;
import hpms.model.*;
import hpms.util.DataStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Doctor Dashboard - displays today's appointments, pending requests, and assigned patients
 */
public class DoctorDashboardPanel extends JPanel {
    private AuthSession session;
    private JTable appointmentsTable;
    private JTable upcomingTable;
    private JTable requestsTable;
    private JTable patientsTable;

    public DoctorDashboardPanel(AuthSession session) {
        this.session = session;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Doctor Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(31, 41, 55));
        JLabel subtitle = new JLabel("Welcome, " + session.fullName);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(new Color(107, 114, 128));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Content - 4 sections
        JPanel content = new JPanel(new GridLayout(4, 1, 12, 12));
        content.setOpaque(false);

        content.add(createTodayAppointmentsSection());
        content.add(createUpcomingAppointmentsSection());
        content.add(createPendingRequestsSection());
        content.add(createAssignedPatientsSection());

        add(content, BorderLayout.CENTER);

        // Refresh when panel becomes visible
        this.addHierarchyListener(evt -> {
            if ((evt.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (this.isShowing()) SwingUtilities.invokeLater(this::refresh);
            }
        });
    }

    private JPanel createTodayAppointmentsSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));
        section.setBackground(new Color(248, 249, 250));
        section.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Today's Appointments");
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setForeground(new Color(47, 111, 237));
        section.add(title, BorderLayout.NORTH);

        // Table
        appointmentsTable = new JTable();
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentsTable.setRowHeight(28);
        refreshAppointmentsTable();

        JScrollPane scroll = new JScrollPane(appointmentsTable);
        scroll.setBorder(null);
        section.add(scroll, BorderLayout.CENTER);

        return section;
    }

    private JPanel createPendingRequestsSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));
        section.setBackground(new Color(248, 249, 250));
        section.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Pending Appointment Requests");
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setForeground(new Color(47, 111, 237));
        section.add(title, BorderLayout.NORTH);

        requestsTable = new JTable();
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.setRowHeight(28);
        refreshRequestsTable();

        JScrollPane scroll = new JScrollPane(requestsTable);
        scroll.setBorder(null);
        section.add(scroll, BorderLayout.CENTER);

        return section;
    }

    private JPanel createAssignedPatientsSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));
        section.setBackground(new Color(248, 249, 250));
        section.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Assigned Patients");
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setForeground(new Color(47, 111, 237));
        section.add(title, BorderLayout.NORTH);

        patientsTable = new JTable();
        patientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientsTable.setRowHeight(28);
        refreshPatientsTable();

        JScrollPane scroll = new JScrollPane(patientsTable);
        scroll.setBorder(null);
        section.add(scroll, BorderLayout.CENTER);

        return section;
    }

    private void refreshAppointmentsTable() {
        LocalDate today = LocalDate.now();
        java.util.List<Appointment> todayAppts = new ArrayList<>();
        
        for (Appointment a : DataStore.appointments.values()) {
            if (session.userId.equals(a.staffId) && a.dateTime.toLocalDate().equals(today)) {
                todayAppts.add(a);
            }
        }

        todayAppts.sort((a, b) -> a.dateTime.compareTo(b.dateTime));

        String[] columns = {"Time", "Patient ID", "Patient Name", "Department"};
        Object[][] data = new Object[todayAppts.size()][4];

        for (int i = 0; i < todayAppts.size(); i++) {
            Appointment a = todayAppts.get(i);
            Patient p = DataStore.patients.get(a.patientId);
            data[i][0] = a.dateTime.toLocalTime().toString();
            data[i][1] = a.patientId;
            data[i][2] = p != null ? p.name : "Unknown";
            data[i][3] = a.department;
        }

        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private JPanel createUpcomingAppointmentsSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));
        section.setBackground(new Color(248, 249, 250));
        section.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Upcoming Week");
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setForeground(new Color(47, 111, 237));
        section.add(title, BorderLayout.NORTH);

        upcomingTable = new JTable();
        upcomingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        upcomingTable.setRowHeight(28);
        refreshUpcomingTable();

        JScrollPane scroll = new JScrollPane(upcomingTable);
        scroll.setBorder(null);
        section.add(scroll, BorderLayout.CENTER);

        return section;
    }

    private void refreshUpcomingTable() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        java.util.List<Appointment> upcoming = new ArrayList<>();

        for (Appointment a : DataStore.appointments.values()) {
            LocalDate d = a.dateTime.toLocalDate();
            if (session.userId.equals(a.staffId) && d.isAfter(today) && d.isBefore(nextWeek)) {
                upcoming.add(a);
            }
        }

        upcoming.sort((a, b) -> a.dateTime.compareTo(b.dateTime));

        String[] columns = {"Date", "Time", "Patient ID", "Patient Name", "Department"};
        Object[][] data = new Object[upcoming.size()][5];

        for (int i = 0; i < upcoming.size(); i++) {
            Appointment a = upcoming.get(i);
            Patient p = DataStore.patients.get(a.patientId);
            data[i][0] = a.dateTime.toLocalDate().toString();
            data[i][1] = a.dateTime.toLocalTime().toString();
            data[i][2] = a.patientId;
            data[i][3] = p != null ? p.name : "Unknown";
            data[i][4] = a.department;
        }

        upcomingTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private void refreshRequestsTable() {
        java.util.List<Appointment> pending = new ArrayList<>();
        for (Appointment a : DataStore.appointments.values()) {
            if (session.userId.equals(a.staffId) && a.notes != null && a.notes.toLowerCase().contains("pending")) {
                pending.add(a);
            }
        }
        pending.sort((a,b) -> a.dateTime.compareTo(b.dateTime));
        String[] columns = {"ID", "Patient", "Requested Date", "Department", "Status"};
        Object[][] data = new Object[pending.size()][5];
        for (int i=0;i<pending.size();i++) {
            Appointment a = pending.get(i);
            Patient p = DataStore.patients.get(a.patientId);
            data[i][0] = a.id;
            data[i][1] = (p!=null?p.name:"Unknown") + " (" + a.patientId + ")";
            data[i][2] = a.dateTime.toString();
            data[i][3] = a.department;
            data[i][4] = "Pending";
        }
        requestsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private void refreshPatientsTable() {
        // Get all unique patients from appointments for this doctor
        java.util.Set<String> patientIds = new LinkedHashSet<>();
        
        for (Appointment a : DataStore.appointments.values()) {
            if (session.userId.equals(a.staffId)) {
                patientIds.add(a.patientId);
            }
        }

        String[] columns = {"Patient ID", "Name", "Age", "Gender", "Contact"};
        Object[][] data = new Object[patientIds.size()][5];

        int i = 0;
        for (String pId : patientIds) {
            Patient p = DataStore.patients.get(pId);
            if (p != null) {
                data[i][0] = p.id;
                data[i][1] = p.name;
                data[i][2] = p.age;
                data[i][3] = p.gender != null ? p.gender.name() : "";
                data[i][4] = p.contact;
                i++;
            }
        }

        patientsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    public void refresh() {
        refreshAppointmentsTable();
        if (upcomingTable != null) refreshUpcomingTable();
        refreshRequestsTable();
        refreshPatientsTable();
    }
}
