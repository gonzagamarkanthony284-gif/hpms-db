package hpms.ui.panels;

import hpms.model.*;
import hpms.util.DataStore;
import hpms.ui.components.RoundedCard;
import hpms.ui.components.SectionHeader;
import hpms.ui.components.Theme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);
        add(SectionHeader.info("Dashboard", "Overview: appointments, admissions, billing summaries."),
                BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setBackground(Theme.BACKGROUND);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(grid, BorderLayout.CENTER);

        grid.add(createCard("Patients", String.valueOf(DataStore.patients.size())));
        long todaysAppts = DataStore.appointments.values().stream()
                .filter(a -> a.dateTime.toLocalDate().equals(LocalDate.now())).count();
        grid.add(createCard("Appointments Today", String.valueOf(todaysAppts)));
        long occupied = DataStore.rooms.values().stream().filter(r -> r.status == RoomStatus.OCCUPIED).count();
        long total = DataStore.rooms.size();
        grid.add(createCard("Bed Occupancy", occupied + "/" + total));

        // Count staff by role
        long doctors = DataStore.staff.values().stream().filter(s -> s.role == StaffRole.DOCTOR).count();
        long nurses = DataStore.staff.values().stream().filter(s -> s.role == StaffRole.NURSE).count();
        long cashiers = DataStore.staff.values().stream().filter(s -> s.role == StaffRole.CASHIER).count();

        grid.add(createCard("Doctors", String.valueOf(doctors)));
        grid.add(createCard("Nurses", String.valueOf(nurses)));
        grid.add(createCard("Cashiers", String.valueOf(cashiers)));
    }

    private JComponent createCard(String title, String value) {
        RoundedCard card = new RoundedCard(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Theme.FOREGROUND);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        valueLabel.setForeground(Theme.PRIMARY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }
}
